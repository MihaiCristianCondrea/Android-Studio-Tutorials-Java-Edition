package com.d4rk.androidtutorials.java.ui.screens.main.repository;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;

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
public class MainRepository implements com.d4rk.androidtutorials.java.data.repository.MainRepository {

    private final Context context;
    private final SharedPreferences defaultSharedPrefs;
    private final AppUpdateManager appUpdateManager;

    public MainRepository(Context context) {
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
     * Check if a given package name is installed.
     *
     * @param packageManager The PackageManager used to check installations.
     * @param packageName    The package to check.
     * @return True if installed, false otherwise.
     */
    public boolean isAppInstalled(PackageManager packageManager, String packageName) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
    public String getBottomNavLabelVisibility(String labelKey, String labelDefaultValue) {
        return defaultSharedPrefs.getString(labelKey, labelDefaultValue);
    }

    /**
     * Retrieves which tab should be shown by default.
     */
    public String getDefaultTabPreference(String defaultTabKey, String defaultTabValue) {
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
    public void applyLanguageSettings() {
        String languageKey = context.getString(R.string.key_language);
        String defaultLanguageValue = context.getString(R.string.default_value_language);
        String languageCode = defaultSharedPrefs.getString(languageKey, defaultLanguageValue);

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode));
    }

    /**
     * Returns the AppUpdateManager so the ViewModel or UI layer can trigger or check updates if needed.
     */
    public AppUpdateManager getAppUpdateManager() {
        return appUpdateManager;
    }

    /**
     * Creates an intent to open your other app or fallback to Google Play if not installed.
     */
    public Intent buildShortcutIntent(boolean isInstalled) {
        if (isInstalled) {
            return new Intent(Intent.ACTION_MAIN)  // <--- set an ACTION
                    .addCategory(Intent.CATEGORY_LAUNCHER) // optional for typical launcher scenario
                    .setClassName("com.d4rk.androidtutorials", "com.d4rk.androidtutorials.MainActivity")
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            return new Intent(Intent.ACTION_VIEW)
                    .setData(Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.androidtutorials"))
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
    }
}