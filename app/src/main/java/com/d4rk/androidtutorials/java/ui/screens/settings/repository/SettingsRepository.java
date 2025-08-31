package com.d4rk.androidtutorials.java.ui.screens.settings.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import java.util.EnumMap;
import java.util.Map;

import com.d4rk.androidtutorials.java.R;

/**
 * Repository that handles reading/writing preferences (e.g., theme, language)
 * and applies the changes (e.g., calls setDefaultNightMode).
 */
public class SettingsRepository implements com.d4rk.androidtutorials.java.data.repository.SettingsRepository {

    private final Context context;
    private final SharedPreferences sharedPreferences;

    public SettingsRepository(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    private static int getNewNightMode(int currentNightMode, String preference, String[] darkModeValues) {
        int newNightMode = currentNightMode;
        if (preference.equals(darkModeValues[0])) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        } else if (preference.equals(darkModeValues[1])) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (preference.equals(darkModeValues[2])) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else if (preference.equals(darkModeValues[3])) {
            newNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
        }
        return newNightMode;
    }

    /**
     * Applies the theme based on the user's preference. Returns true if the theme actually changed.
     */
    public boolean applyTheme() {
        String[] darkModeValues = context.getResources().getStringArray(R.array.preference_theme_values);
        String preferenceKey = context.getString(R.string.key_theme);
        String defaultThemeValue = context.getString(R.string.default_value_theme);

        String preference = sharedPreferences.getString(preferenceKey, defaultThemeValue);
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        int newNightMode = getNewNightMode(currentNightMode, preference, darkModeValues);
        if (newNightMode != currentNightMode) {
            AppCompatDelegate.setDefaultNightMode(newNightMode);
            return true;
        }
        return false;
    }

    /**
     * Applies the language from prefs. If the user changes language, this is how you’d do it.
     */
    public void applyLanguage() {
        String languageKey = context.getString(R.string.key_language);
        String defaultLanguageValue = context.getString(R.string.default_value_language);

        String languageCode = sharedPreferences.getString(languageKey, defaultLanguageValue);
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode));
    }

    /**
     * Handle a preference change for a given key. For example, theme or language.
     */
    public void handlePreferenceChange(String key) {
        if (key == null) return;

        if (key.equals(context.getString(R.string.key_theme))) {
            applyTheme();
        } else if (key.equals(context.getString(R.string.key_language))) {
            applyLanguage();
        } else if (key.equals(context.getString(R.string.key_consent_analytics)) ||
                key.equals(context.getString(R.string.key_consent_ad_storage)) ||
                key.equals(context.getString(R.string.key_consent_ad_user_data)) ||
                key.equals(context.getString(R.string.key_consent_ad_personalization))) {
            applyConsent();
        }
    }

    /** Apply Firebase consent settings from preferences */
    public void applyConsent() {
        boolean analytics = sharedPreferences.getBoolean(
                context.getString(R.string.key_consent_analytics), true);
        boolean adStorage = sharedPreferences.getBoolean(
                context.getString(R.string.key_consent_ad_storage), true);
        boolean adUserData = sharedPreferences.getBoolean(
                context.getString(R.string.key_consent_ad_user_data), true);
        boolean adPersonalization = sharedPreferences.getBoolean(
                context.getString(R.string.key_consent_ad_personalization), true);

        Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> map =
                new EnumMap<>(FirebaseAnalytics.ConsentType.class);
        map.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE,
                analytics ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        map.put(FirebaseAnalytics.ConsentType.AD_STORAGE,
                adStorage ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        map.put(FirebaseAnalytics.ConsentType.AD_USER_DATA,
                adUserData ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        map.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION,
                adPersonalization ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);

        FirebaseAnalytics.getInstance(context).setConsent(map);
    }

    @Override
    public void registerPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public void unregisterPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    @Override
    public String getDarkMode() {
        String preferenceKey = context.getString(R.string.key_theme);
        String defaultThemeValue = context.getString(R.string.default_value_theme);
        return sharedPreferences.getString(preferenceKey, defaultThemeValue);
    }

    @Override
    public void setConsentAccepted(boolean accepted) {
        sharedPreferences.edit()
                .putBoolean(context.getString(R.string.key_consent_analytics), accepted)
                .apply();
    }
}