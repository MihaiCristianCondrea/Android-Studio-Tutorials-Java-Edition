package com.d4rk.androidtutorials.java.data.repository;

import android.content.Intent;
import android.content.pm.PackageManager;
import com.google.android.play.core.appupdate.AppUpdateManager;

public interface MainRepository {
    boolean isAppInstalled(PackageManager packageManager, String packageName);
    boolean applyThemeSettings(String[] darkModeValues);
    String getBottomNavLabelVisibility(String labelKey, String labelDefaultValue);
    String getDefaultTabPreference(String defaultTabKey, String defaultTabValue);
    boolean shouldShowStartupScreen();
    void markStartupScreenShown();
    void applyLanguageSettings();
    AppUpdateManager getAppUpdateManager();
    Intent buildShortcutIntent(boolean isInstalled);
}
