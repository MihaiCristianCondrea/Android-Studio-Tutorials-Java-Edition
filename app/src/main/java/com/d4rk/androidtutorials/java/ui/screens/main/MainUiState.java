package com.d4rk.androidtutorials.java.ui.screens.main;

/**
 * UI state for {@link MainActivity}. Holds values related to the main screen such as
 * bottom navigation visibility, the default navigation destination, and whether the theme
 * has changed requiring a recreation of the activity.
 */
public class MainUiState {
    private final int bottomNavVisibility;
    private final int defaultNavDestination;
    private final boolean themeChanged;

    public MainUiState(int bottomNavVisibility, int defaultNavDestination, boolean themeChanged) {
        this.bottomNavVisibility = bottomNavVisibility;
        this.defaultNavDestination = defaultNavDestination;
        this.themeChanged = themeChanged;
    }

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
