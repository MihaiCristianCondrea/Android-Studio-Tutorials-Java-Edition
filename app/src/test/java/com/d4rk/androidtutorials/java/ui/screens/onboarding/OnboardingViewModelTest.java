package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OnboardingViewModelTest {

    private static final String THEME_KEY = "theme";
    private static final String DEFAULT_TAB_KEY = "default_tab";
    private static final String BOTTOM_NAV_LABELS_KEY = "labels";
    private static final String MONOSPACE_FONT_KEY = "monospace_font";
    private static final String CRASHLYTICS_KEY = "firebase_crashlytics";
    private static final String CONSENT_ANALYTICS_KEY = "consent_analytics_storage";
    private static final String CONSENT_AD_STORAGE_KEY = "consent_ad_storage";
    private static final String CONSENT_AD_USER_DATA_KEY = "consent_ad_user_data";
    private static final String CONSENT_AD_PERSONALIZATION_KEY = "consent_ad_personalization";
    private static final String ONBOARDING_COMPLETE_KEY = "onboarding_complete";

    private static final String[] THEME_VALUES = new String[]{"MODE_NIGHT_FOLLOW_SYSTEM", "MODE_NIGHT_NO"};
    private static final String[] TAB_VALUES = new String[]{"home", "android_studio"};
    private static final String[] BOTTOM_NAV_LABEL_VALUES = new String[]{"labeled", "selected"};
    private static final String[] CODE_FONT_VALUES = new String[]{"0", "1"};

    @Mock
    private Context context;

    @Mock
    private Resources resources;

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor editor;

    private MockedStatic<PreferenceManager> preferenceManagerMock;
    private MockedStatic<AppCompatDelegate> appCompatDelegateMock;

    @Before
    public void setUp() {
        when(context.getResources()).thenReturn(resources);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putString(anyString(), anyString())).thenReturn(editor);
        when(editor.putBoolean(anyString(), anyBoolean())).thenReturn(editor);

        when(context.getString(R.string.key_theme)).thenReturn(THEME_KEY);
        when(context.getString(R.string.key_default_tab)).thenReturn(DEFAULT_TAB_KEY);
        when(context.getString(R.string.key_bottom_navigation_bar_labels)).thenReturn(BOTTOM_NAV_LABELS_KEY);
        when(context.getString(R.string.key_monospace_font)).thenReturn(MONOSPACE_FONT_KEY);
        when(context.getString(R.string.key_firebase_crashlytics)).thenReturn(CRASHLYTICS_KEY);
        when(context.getString(R.string.key_consent_analytics)).thenReturn(CONSENT_ANALYTICS_KEY);
        when(context.getString(R.string.key_consent_ad_storage)).thenReturn(CONSENT_AD_STORAGE_KEY);
        when(context.getString(R.string.key_consent_ad_user_data)).thenReturn(CONSENT_AD_USER_DATA_KEY);
        when(context.getString(R.string.key_consent_ad_personalization)).thenReturn(CONSENT_AD_PERSONALIZATION_KEY);
        when(context.getString(R.string.key_onboarding_complete)).thenReturn(ONBOARDING_COMPLETE_KEY);

        when(resources.getStringArray(R.array.preference_theme_values)).thenReturn(THEME_VALUES);
        when(resources.getStringArray(R.array.preference_default_tab_values)).thenReturn(TAB_VALUES);
        when(resources.getStringArray(R.array.preference_bottom_navigation_bar_labels_values)).thenReturn(BOTTOM_NAV_LABEL_VALUES);
        when(resources.getStringArray(R.array.code_font_values)).thenReturn(CODE_FONT_VALUES);

        preferenceManagerMock = mockStatic(PreferenceManager.class);
        preferenceManagerMock.when(() -> PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);

        appCompatDelegateMock = mockStatic(AppCompatDelegate.class);
    }

    @After
    public void tearDown() {
        preferenceManagerMock.close();
        appCompatDelegateMock.close();
    }

    private OnboardingViewModel createViewModelWithExistingPrefs() {
        when(sharedPreferences.contains(THEME_KEY)).thenReturn(true);
        when(sharedPreferences.contains(DEFAULT_TAB_KEY)).thenReturn(true);
        return new OnboardingViewModel(context);
    }

    @Test
    public void constructor_initializesDefaults_whenPreferencesMissing() {
        when(sharedPreferences.contains(THEME_KEY)).thenReturn(false);
        when(sharedPreferences.contains(DEFAULT_TAB_KEY)).thenReturn(false);

        new OnboardingViewModel(context);

        appCompatDelegateMock.verify(() -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM));
        verify(editor).putString(THEME_KEY, THEME_VALUES[0]);
        verify(editor).putString(DEFAULT_TAB_KEY, TAB_VALUES[0]);
        verify(editor, times(2)).apply();
    }

    @Test
    public void setTheme_persistsValue_andGetThemeReturnsValue() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();
        clearInvocations(editor, sharedPreferences);

        String newTheme = THEME_VALUES[1];
        viewModel.setTheme(newTheme);

        verify(editor).putString(THEME_KEY, newTheme);
        verify(editor).apply();

        when(sharedPreferences.getString(THEME_KEY, THEME_VALUES[0])).thenReturn(newTheme);

        assertEquals(newTheme, viewModel.getTheme());
        verify(sharedPreferences).getString(THEME_KEY, THEME_VALUES[0]);
    }

    @Test
    public void setDefaultTab_persistsValue() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();
        clearInvocations(editor);

        String newTab = TAB_VALUES[1];
        viewModel.setDefaultTab(newTab);

        verify(editor).putString(DEFAULT_TAB_KEY, newTab);
        verify(editor).apply();
    }

    @Test
    public void setBottomNavLabels_persistsValue_andGetBottomNavLabelsReturnsValue() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();
        clearInvocations(editor, sharedPreferences);

        String newValue = BOTTOM_NAV_LABEL_VALUES[1];
        viewModel.setBottomNavLabels(newValue);

        verify(editor).putString(BOTTOM_NAV_LABELS_KEY, newValue);
        verify(editor).apply();

        when(sharedPreferences.getString(BOTTOM_NAV_LABELS_KEY, BOTTOM_NAV_LABEL_VALUES[0])).thenReturn(newValue);

        assertEquals(newValue, viewModel.getBottomNavLabels());
        verify(sharedPreferences).getString(BOTTOM_NAV_LABELS_KEY, BOTTOM_NAV_LABEL_VALUES[0]);
    }

    @Test
    public void setMonospaceFont_persistsValue_andGetMonospaceFontReturnsValue() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();
        clearInvocations(editor, sharedPreferences);

        String newFont = CODE_FONT_VALUES[1];
        viewModel.setMonospaceFont(newFont);

        verify(editor).putString(MONOSPACE_FONT_KEY, newFont);
        verify(editor).apply();

        when(sharedPreferences.getString(MONOSPACE_FONT_KEY, CODE_FONT_VALUES[0])).thenReturn(newFont);

        assertEquals(newFont, viewModel.getMonospaceFont());
        verify(sharedPreferences).getString(MONOSPACE_FONT_KEY, CODE_FONT_VALUES[0]);
    }

    @Test
    public void booleanSetters_persistValues() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();
        clearInvocations(editor);

        viewModel.setCrashlyticsEnabled(true);
        verify(editor).putBoolean(CRASHLYTICS_KEY, true);

        viewModel.setConsentAnalytics(false);
        verify(editor).putBoolean(CONSENT_ANALYTICS_KEY, false);

        viewModel.setConsentAdStorage(true);
        verify(editor).putBoolean(CONSENT_AD_STORAGE_KEY, true);

        viewModel.setConsentAdUserData(false);
        verify(editor).putBoolean(CONSENT_AD_USER_DATA_KEY, false);

        viewModel.setConsentAdPersonalization(true);
        verify(editor).putBoolean(CONSENT_AD_PERSONALIZATION_KEY, true);

        verify(editor, times(5)).apply();
    }

    @Test
    public void markOnboardingComplete_persistsFlag() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();
        clearInvocations(editor);

        viewModel.markOnboardingComplete();

        verify(editor).putBoolean(ONBOARDING_COMPLETE_KEY, true);
        verify(editor).apply();
    }

    @Test
    public void setCurrentPage_updatesState() {
        OnboardingViewModel viewModel = createViewModelWithExistingPrefs();

        viewModel.setCurrentPage(2);

        assertEquals(2, viewModel.getCurrentPage());
    }
}
