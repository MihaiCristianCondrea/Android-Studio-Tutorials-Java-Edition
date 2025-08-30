package com.d4rk.androidtutorials.java.domain.settings;

import com.d4rk.androidtutorials.java.ui.screens.settings.repository.SettingsRepository;

/** Handles a preference change and returns true if theme changed. */
public class OnPreferenceChangedUseCase {
    private final SettingsRepository repository;

    public OnPreferenceChangedUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public boolean invoke(String key) {
        repository.handlePreferenceChange(key);
        return repository.applyTheme();
    }
}
