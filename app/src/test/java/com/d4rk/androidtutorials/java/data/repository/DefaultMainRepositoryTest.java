package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DefaultMainRepositoryTest {

    private Context context;
    private FakeSharedPreferences defaultPrefs;
    private AppUpdateManager updateManager;
    private MockedStatic<PreferenceManager> prefManager;
    private MockedStatic<AppUpdateManagerFactory> updateManagerFactory;
    private DefaultMainRepository repository;

    @Before
    public void setUp() {
        context = mock(Context.class);
        defaultPrefs = new FakeSharedPreferences();
        FakeSharedPreferences startupPrefs = new FakeSharedPreferences();
        updateManager = mock(AppUpdateManager.class);

        prefManager = mockStatic(PreferenceManager.class);
        prefManager.when(() -> PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(defaultPrefs);

        updateManagerFactory = mockStatic(AppUpdateManagerFactory.class);
        updateManagerFactory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(updateManager);

        when(context.getApplicationContext()).thenReturn(context);
        when(context.getString(R.string.key_theme)).thenReturn("theme");
        when(context.getString(R.string.default_value_theme)).thenReturn("system");
        when(context.getString(R.string.key_bottom_navigation_bar_labels)).thenReturn("labels");
        when(context.getString(R.string.default_value_bottom_navigation_bar_labels)).thenReturn("never");
        when(context.getString(R.string.key_default_tab)).thenReturn("tab");
        when(context.getString(R.string.default_value_tab)).thenReturn("home");
        when(context.getString(R.string.key_language)).thenReturn("language");
        when(context.getString(R.string.default_value_language)).thenReturn("en");
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(startupPrefs);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());

        repository = new DefaultMainRepository(context);
    }

    @After
    public void tearDown() {
        prefManager.close();
        updateManagerFactory.close();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList());
    }

    @Test
    public void applyThemeSettings_noChangeWhenPreferenceSame() {
        defaultPrefs.edit().putString("theme", "light").apply();

        try (MockedStatic<AppCompatDelegate> appCompatDelegate = mockStatic(AppCompatDelegate.class)) {
            appCompatDelegate.when(AppCompatDelegate::getDefaultNightMode).thenReturn(AppCompatDelegate.MODE_NIGHT_NO);

            boolean changed = repository.applyThemeSettings(new String[]{"system", "light", "dark", "auto"});

            assertFalse(changed);
            appCompatDelegate.verify(AppCompatDelegate::getDefaultNightMode);
            appCompatDelegate.verifyNoMoreInteractions();
        }
    }

    @Test
    public void applyThemeSettings_changesWhenPreferenceDiffers() {
        defaultPrefs.edit().putString("theme", "dark").apply();

        try (MockedStatic<AppCompatDelegate> appCompatDelegate = mockStatic(AppCompatDelegate.class)) {
            appCompatDelegate.when(AppCompatDelegate::getDefaultNightMode).thenReturn(AppCompatDelegate.MODE_NIGHT_NO);

            boolean changed = repository.applyThemeSettings(new String[]{"system", "light", "dark", "auto"});

            assertTrue(changed);
            appCompatDelegate.verify(AppCompatDelegate::getDefaultNightMode);
            appCompatDelegate.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES));
            appCompatDelegate.verifyNoMoreInteractions();
        }
    }

    @Test
    public void getBottomNavLabelVisibilityAndDefaultTabPreferenceReturnStoredValues() {
        defaultPrefs.edit().putString("labels", "always").apply();
        defaultPrefs.edit().putString("tab", "search").apply();
        assertEquals("always", repository.getBottomNavLabelVisibility());
        assertEquals("search", repository.getDefaultTabPreference());
    }

    @Test
    public void getBottomNavLabelVisibilityAndDefaultTabPreferenceReturnDefaultValues() {
        assertEquals("never", repository.getBottomNavLabelVisibility());
        assertEquals("home", repository.getDefaultTabPreference());
    }

    @Test
    public void startupScreenDefaultsTrueAndFlipsAfterMark() {
        assertTrue(repository.shouldShowStartupScreen());
        repository.markStartupScreenShown();
        assertFalse(repository.shouldShowStartupScreen());
    }

    @Test
    public void applyLanguageSettings_setsLocales() {
        defaultPrefs.edit().putString("language", "fr").apply();

        final LocaleListCompat[] capturedLocale = new LocaleListCompat[1];

        try (MockedStatic<AppCompatDelegate> appCompatDelegate = mockStatic(AppCompatDelegate.class)) {
            appCompatDelegate
                    .when(() -> AppCompatDelegate.setApplicationLocales(any(LocaleListCompat.class)))
                    .thenAnswer(invocation -> {
                        capturedLocale[0] = invocation.getArgument(0);
                        return null;
                    });

            repository.applyLanguageSettings();

            assertNotNull(capturedLocale[0]);
            assertEquals("fr", capturedLocale[0].toLanguageTags());
            appCompatDelegate.verify(() -> AppCompatDelegate.setApplicationLocales(capturedLocale[0]));
            appCompatDelegate.verifyNoMoreInteractions();
        }
    }

    @Test
    public void getAppUpdateManager_returnsInjectedManager() {
        assertSame(updateManager, repository.getAppUpdateManager());
    }

    private static class FakeSharedPreferences implements SharedPreferences {
        private final Map<String, Object> values = new HashMap<>();

        @Override
        public Map<String, ?> getAll() {
            return values;
        }

        @Override
        public String getString(String key, String defValue) {
            Object v = values.get(key);
            return v instanceof String ? (String) v : defValue;
        }

        @SuppressWarnings("unchecked")
        @Override
        public Set<String> getStringSet(String key, Set<String> defValues) {
            Object v = values.get(key);
            return v instanceof Set ? (Set<String>) v : defValues;
        }

        @Override
        public int getInt(String key, int defValue) {
            Object v = values.get(key);
            return v instanceof Integer ? (Integer) v : defValue;
        }

        @Override
        public long getLong(String key, long defValue) {
            Object v = values.get(key);
            return v instanceof Long ? (Long) v : defValue;
        }

        @Override
        public float getFloat(String key, float defValue) {
            Object v = values.get(key);
            return v instanceof Float ? (Float) v : defValue;
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            Object v = values.get(key);
            return v instanceof Boolean ? (Boolean) v : defValue;
        }

        @Override
        public boolean contains(String key) {
            return values.containsKey(key);
        }

        @Override
        public Editor edit() {
            return new Editor() {
                @Override
                public Editor putString(String key, String value) {
                    values.put(key, value);
                    return this;
                }

                @Override
                public Editor putStringSet(String key, Set<String> value) {
                    values.put(key, value);
                    return this;
                }

                @Override
                public Editor putInt(String key, int value) {
                    values.put(key, value);
                    return this;
                }

                @Override
                public Editor putLong(String key, long value) {
                    values.put(key, value);
                    return this;
                }

                @Override
                public Editor putFloat(String key, float value) {
                    values.put(key, value);
                    return this;
                }

                @Override
                public Editor putBoolean(String key, boolean value) {
                    values.put(key, value);
                    return this;
                }

                @Override
                public Editor remove(String key) {
                    values.remove(key);
                    return this;
                }

                @Override
                public Editor clear() {
                    values.clear();
                    return this;
                }

                @Override
                public boolean commit() {
                    return true;
                }

                @Override
                public void apply() {
                }
            };
        }

        @Override
        public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        }

        @Override
        public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        }
    }
}
