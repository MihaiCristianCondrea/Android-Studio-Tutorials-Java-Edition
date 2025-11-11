package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

/**
 * Repository class that handles data operations such as SharedPreferences,
 * app update checks, etc.
 */
public class DefaultMainRepository implements MainRepository {

    private final Context context;
    private final SharedPreferences defaultSharedPrefs;
    private final AppUpdateManager appUpdateManager;

    public DefaultMainRepository(Context context) {
        this.context = context.getApplicationContext();
        this.defaultSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.appUpdateManager = AppUpdateManagerFactory.create(this.context);
    }

    private static int getNewNightMode(String[] darkModeValues, int currentNightMode, @androidx.annotation.Nullable String preference) {
        int newNightMode = currentNightMode;

        if (preference != null) {
            if (preference.equals(darkModeValues[0])) {
                newNightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
            } else if (preference.equals(darkModeValues[1])) {
                newNightMode = AppCompatDelegate.MODE_NIGHT_NO;
            } else if (preference.equals(darkModeValues[2])) {
                newNightMode = AppCompatDelegate.MODE_NIGHT_YES;
            } else if (preference.equals(darkModeValues[3])) {
                newNightMode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
            }
        }
        return newNightMode;
    }

    /**
     * Retrieves the user's theme preference and applies it.
     * This method returns true if a change was applied (so the Activity can decide if it needs to recreate).
     */
    public boolean applyThemeSettings(String[] darkModeValues) {
        String preferenceKey = context.getString(R.string.key_theme);
        String defaultThemeValue = context.getString(R.string.default_value_theme);

        String preference = defaultSharedPrefs.getString(preferenceKey, defaultThemeValue);
        int currentNightMode = AppCompatDelegate.getDefaultNightMode();
        int newNightMode = getNewNightMode(darkModeValues, currentNightMode, preference);

        if (newNightMode != currentNightMode) {
            AppCompatDelegate.setDefaultNightMode(newNightMode);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the bottom navigation label visibility preference.
     */
    public String getBottomNavLabelVisibility() {
        String labelKey = context.getString(R.string.key_bottom_navigation_bar_labels);
        String labelDefaultValue = context.getString(R.string.default_value_bottom_navigation_bar_labels);
        return defaultSharedPrefs.getString(labelKey, labelDefaultValue);
    }

    /**
     * Retrieves which tab should be shown by default.
     */
    public String getDefaultTabPreference() {
        String defaultTabKey = context.getString(R.string.key_default_tab);
        String defaultTabValue = context.getString(R.string.default_value_tab);
        return defaultSharedPrefs.getString(defaultTabKey, defaultTabValue);
    }

    /**
     * Returns true if we should show the startup screen (first launch).
     */
    public boolean shouldShowStartupScreen() {
        SharedPreferences startupPreference = context.getSharedPreferences("startup", Context.MODE_PRIVATE);
        return startupPreference.getBoolean("value", true);
    }

    /**
     * Mark that the startup screen has been shown.
     */
    public void markStartupScreenShown() {
        SharedPreferences startupPreference = context.getSharedPreferences("startup", Context.MODE_PRIVATE);
        startupPreference.edit().putBoolean("value", false).apply();
    }

    /**
     * Applies the language setting from SharedPreferences.
     */
    public boolean applyLanguageSettings() {
        String languageKey = context.getString(R.string.key_language);
        String defaultLanguageValue = context.getString(R.string.default_value_language);
        String languageCode = defaultSharedPrefs.getString(languageKey, defaultLanguageValue);

        if (languageCode == null) {
            languageCode = defaultLanguageValue;
        }

        LocaleListCompat currentLocales = AppCompatDelegate.getApplicationLocales();
        String currentLanguageTags = currentLocales.toLanguageTags();
        if (languageCode.equals(currentLanguageTags)) {
            return false;
        }

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode));
        return true;
    }

    /**
     * Returns the AppUpdateManager so the ViewModel or UI layer can trigger or check updates if needed.
     */
    public AppUpdateManager getAppUpdateManager() {
        return appUpdateManager;
    }

}