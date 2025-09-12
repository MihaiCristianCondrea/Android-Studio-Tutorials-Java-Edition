package com.d4rk.androidtutorials.java.ui.screens.main;

import com.google.android.material.navigation.NavigationBarView;

/**
 * UI state for {@link MainActivity}. Holds values related to the main screen such as
 * bottom navigation visibility, the default navigation destination, and whether the theme
 * has changed requiring a recreation of the activity.
 */
public record MainUiState(@NavigationBarView.LabelVisibility int bottomNavVisibility,
                          int defaultNavDestination, boolean themeChanged) {
}
