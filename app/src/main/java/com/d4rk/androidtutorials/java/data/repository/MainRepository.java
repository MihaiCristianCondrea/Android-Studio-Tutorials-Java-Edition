package com.d4rk.androidtutorials.java.data.repository;

import com.google.android.play.core.appupdate.AppUpdateManager;

public interface MainRepository {
    boolean applyThemeSettings(String[] darkModeValues);

    String getBottomNavLabelVisibility();

    String getDefaultTabPreference();

    boolean shouldShowStartupScreen();

    void markStartupScreenShown();

    void applyLanguageSettings();

    AppUpdateManager getAppUpdateManager();
}
