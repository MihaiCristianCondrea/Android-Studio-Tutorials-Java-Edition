package com.d4rk.androidtutorials.java.ui.screens.home;

import java.util.List;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;

/**
 * Represents the UI state for the Home screen.
 */
public record HomeUiState(
        String announcementTitle,
        String announcementSubtitle,
        String dailyTip,
        List<PromotedApp> promotedApps
) {}

