package com.d4rk.androidtutorials.java.ui.screens.home;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import java.util.List;

/**
 * Represents the UI state for the Home screen.
 */
public record HomeUiState(
        String announcementTitle,
        String announcementSubtitle,
        String dailyTip,
        List<PromotedApp> promotedApps
) {}

