package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ui.screens.settings.repository.SettingsRepository;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Map;

public class SettingsRepositoryTest {

    private static final String THEME_KEY = "theme_key";
    private static final String DEFAULT_THEME = "system";
    private static final String[] DARK_MODE_VALUES = new String[]{"system", "light", "dark", "battery"};
    private static final String LANGUAGE_KEY = "language_key";
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String CONSENT_ANALYTICS = "consent_analytics";
    private static final String CONSENT_AD_STORAGE = "consent_ad_storage";
    private static final String CONSENT_AD_USER_DATA = "consent_ad_user_data";
    private static final String CONSENT_AD_PERSONALIZATION = "consent_ad_personalization";

    private SettingsRepository createRepository(Context context,
                                                SharedPreferences sharedPreferences,
                                                Resources resources,
                                                MockedStatic<PreferenceManager> preferenceManagerMockedStatic) {
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getResources()).thenReturn(resources);
        preferenceManagerMockedStatic.when(() -> PreferenceManager.getDefaultSharedPreferences(context))
                .thenReturn(sharedPreferences);
        return new SettingsRepository(context);
    }

    private void stubCommonStrings(Context context, Resources resources) {
        when(context.getString(R.string.key_theme)).thenReturn(THEME_KEY);
        when(context.getString(R.string.default_value_theme)).thenReturn(DEFAULT_THEME);
        when(resources.getStringArray(R.array.preference_theme_values)).thenReturn(DARK_MODE_VALUES);
        when(context.getString(R.string.key_language)).thenReturn(LANGUAGE_KEY);
        when(context.getString(R.string.default_value_language)).thenReturn(DEFAULT_LANGUAGE);
        when(context.getString(R.string.key_consent_analytics)).thenReturn(CONSENT_ANALYTICS);
        when(context.getString(R.string.key_consent_ad_storage)).thenReturn(CONSENT_AD_STORAGE);
        when(context.getString(R.string.key_consent_ad_user_data)).thenReturn(CONSENT_AD_USER_DATA);
        when(context.getString(R.string.key_consent_ad_personalization)).thenReturn(CONSENT_AD_PERSONALIZATION);
    }

    @Test
    public void applyTheme_updatesNightModeWhenPreferenceDiffers() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        when(sharedPreferences.getString(THEME_KEY, DEFAULT_THEME)).thenReturn(null);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class);
             MockedStatic<AppCompatDelegate> appCompatDelegateMockedStatic = Mockito.mockStatic(AppCompatDelegate.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            appCompatDelegateMockedStatic.when(AppCompatDelegate::getDefaultNightMode)
                    .thenReturn(AppCompatDelegate.MODE_NIGHT_YES);

            boolean changed = repository.applyTheme();

            assertTrue(changed);
            appCompatDelegateMockedStatic.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        }
    }

    @Test
    public void applyTheme_returnsFalseWhenModeSame() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        when(sharedPreferences.getString(THEME_KEY, DEFAULT_THEME)).thenReturn(DARK_MODE_VALUES[1]);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class);
             MockedStatic<AppCompatDelegate> appCompatDelegateMockedStatic = Mockito.mockStatic(AppCompatDelegate.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            appCompatDelegateMockedStatic.when(AppCompatDelegate::getDefaultNightMode)
                    .thenReturn(AppCompatDelegate.MODE_NIGHT_NO);

            boolean changed = repository.applyTheme();

            assertFalse(changed);
            appCompatDelegateMockedStatic.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM), never());
            appCompatDelegateMockedStatic.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO), never());
            appCompatDelegateMockedStatic.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES), never());
            appCompatDelegateMockedStatic.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY), never());
        }
    }

    @Test
    public void handlePreferenceChange_nullKeyDoesNothing() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);
            SettingsRepository spyRepository = Mockito.spy(repository);
            doNothing().when(spyRepository).applyTheme();
            doNothing().when(spyRepository).applyLanguage();
            doNothing().when(spyRepository).applyConsent();

            spyRepository.handlePreferenceChange(null);

            verify(spyRepository, never()).applyTheme();
            verify(spyRepository, never()).applyLanguage();
            verify(spyRepository, never()).applyConsent();
        }
    }

    @Test
    public void handlePreferenceChange_themeKeyCallsApplyTheme() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);
            SettingsRepository spyRepository = Mockito.spy(repository);
            doNothing().when(spyRepository).applyTheme();
            doNothing().when(spyRepository).applyLanguage();
            doNothing().when(spyRepository).applyConsent();

            spyRepository.handlePreferenceChange(THEME_KEY);

            verify(spyRepository).applyTheme();
            verify(spyRepository, never()).applyLanguage();
            verify(spyRepository, never()).applyConsent();
        }
    }

    @Test
    public void handlePreferenceChange_languageKeyCallsApplyLanguage() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);
            SettingsRepository spyRepository = Mockito.spy(repository);
            doNothing().when(spyRepository).applyTheme();
            doNothing().when(spyRepository).applyLanguage();
            doNothing().when(spyRepository).applyConsent();

            spyRepository.handlePreferenceChange(LANGUAGE_KEY);

            verify(spyRepository).applyLanguage();
            verify(spyRepository, never()).applyTheme();
            verify(spyRepository, never()).applyConsent();
        }
    }

    @Test
    public void handlePreferenceChange_consentKeyCallsApplyConsent() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);
            SettingsRepository spyRepository = Mockito.spy(repository);
            doNothing().when(spyRepository).applyTheme();
            doNothing().when(spyRepository).applyLanguage();
            doNothing().when(spyRepository).applyConsent();

            spyRepository.handlePreferenceChange(CONSENT_AD_USER_DATA);

            verify(spyRepository).applyConsent();
            verify(spyRepository, never()).applyTheme();
            verify(spyRepository, never()).applyLanguage();
        }
    }

    @Test
    public void applyLanguage_setsApplicationLocalesFromPreference() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        when(sharedPreferences.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE)).thenReturn("es");

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class);
             MockedStatic<AppCompatDelegate> appCompatDelegateMockedStatic = Mockito.mockStatic(AppCompatDelegate.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            repository.applyLanguage();

            LocaleListCompat expectedLocales = LocaleListCompat.forLanguageTags("es");
            appCompatDelegateMockedStatic.verify(() -> AppCompatDelegate.setApplicationLocales(expectedLocales));
        }
    }

    @Test
    public void applyConsent_readsPreferencesAndUpdatesFirebase() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        when(sharedPreferences.getBoolean(CONSENT_ANALYTICS, true)).thenReturn(true);
        when(sharedPreferences.getBoolean(CONSENT_AD_STORAGE, true)).thenReturn(false);
        when(sharedPreferences.getBoolean(CONSENT_AD_USER_DATA, true)).thenReturn(true);
        when(sharedPreferences.getBoolean(CONSENT_AD_PERSONALIZATION, true)).thenReturn(false);
        FirebaseAnalytics firebaseAnalytics = mock(FirebaseAnalytics.class);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class);
             MockedStatic<FirebaseAnalytics> firebaseAnalyticsMockedStatic = Mockito.mockStatic(FirebaseAnalytics.class)) {
            firebaseAnalyticsMockedStatic.when(() -> FirebaseAnalytics.getInstance(context))
                    .thenReturn(firebaseAnalytics);
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            repository.applyConsent();

            ArgumentCaptor<Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>> captor =
                    ArgumentCaptor.forClass(Map.class);
            verify(firebaseAnalytics).setConsent(captor.capture());

            Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> value = captor.getValue();
            assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED,
                    value.get(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE));
            assertEquals(FirebaseAnalytics.ConsentStatus.DENIED,
                    value.get(FirebaseAnalytics.ConsentType.AD_STORAGE));
            assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED,
                    value.get(FirebaseAnalytics.ConsentType.AD_USER_DATA));
            assertEquals(FirebaseAnalytics.ConsentStatus.DENIED,
                    value.get(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION));
        }
    }

    @Test
    public void registerPreferenceChangeListener_forwardsCall() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        SharedPreferences.OnSharedPreferenceChangeListener listener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            repository.registerPreferenceChangeListener(listener);

            verify(sharedPreferences).registerOnSharedPreferenceChangeListener(listener);
        }
    }

    @Test
    public void unregisterPreferenceChangeListener_forwardsCall() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        SharedPreferences.OnSharedPreferenceChangeListener listener = mock(SharedPreferences.OnSharedPreferenceChangeListener.class);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            repository.unregisterPreferenceChangeListener(listener);

            verify(sharedPreferences).unregisterOnSharedPreferenceChangeListener(listener);
        }
    }

    @Test
    public void getDarkMode_returnsStoredPreference() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        when(sharedPreferences.getString(THEME_KEY, DEFAULT_THEME)).thenReturn("dark");

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            assertEquals("dark", repository.getDarkMode());
        }
    }

    @Test
    public void setConsentAccepted_updatesSharedPreferences() {
        Context context = mock(Context.class);
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        Resources resources = mock(Resources.class);
        stubCommonStrings(context, resources);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putBoolean(CONSENT_ANALYTICS, true)).thenReturn(editor);

        try (MockedStatic<PreferenceManager> preferenceManagerMockedStatic = Mockito.mockStatic(PreferenceManager.class)) {
            SettingsRepository repository = createRepository(context, sharedPreferences, resources, preferenceManagerMockedStatic);

            repository.setConsentAccepted(true);

            verify(sharedPreferences).edit();
            verify(editor).putBoolean(CONSENT_ANALYTICS, true);
            verify(editor).apply();
        }
    }
}
