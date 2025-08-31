package com.d4rk.androidtutorials.java.ui.screens.main;

import com.google.android.material.navigation.NavigationBarView;

/**
 * UI state for {@link MainActivity}. Holds values related to the main screen such as
 * bottom navigation visibility, the default navigation destination, and whether the theme
 * has changed requiring a recreation of the activity.
 */
public class MainUiState {
    @NavigationBarView.LabelVisibility
    private final int bottomNavVisibility;
    private final int defaultNavDestination;
    private final boolean themeChanged;

    public MainUiState(@NavigationBarView.LabelVisibility int bottomNavVisibility, int defaultNavDestination, boolean themeChanged) {
        this.bottomNavVisibility = bottomNavVisibility;
        this.defaultNavDestination = defaultNavDestination;
        this.themeChanged = themeChanged;
    }

    @NavigationBarView.LabelVisibility
    public int getBottomNavVisibility() {
        return bottomNavVisibility;
    }

    public int getDefaultNavDestination() {
        return defaultNavDestination;
    }

    public boolean isThemeChanged() {
        return themeChanged;
    }
}
