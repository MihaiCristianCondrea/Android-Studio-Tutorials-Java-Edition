package com.d4rk.androidtutorials.java.data.repository;

import android.content.Intent;
import com.google.android.play.core.appupdate.AppUpdateManager;

public interface MainRepository {
    boolean isAppInstalled(String packageName);
    boolean applyThemeSettings(String[] darkModeValues);
    String getBottomNavLabelVisibility();
    String getDefaultTabPreference();
    boolean shouldShowStartupScreen();
    void markStartupScreenShown();
    void applyLanguageSettings();
    AppUpdateManager getAppUpdateManager();
    Intent buildShortcutIntent(boolean isInstalled);
}
