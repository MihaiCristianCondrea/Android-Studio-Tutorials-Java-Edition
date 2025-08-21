package com.d4rk.androidtutorials.java.domain.settings;

import com.d4rk.androidtutorials.java.ui.screens.settings.repository.SettingsRepository;

/** Applies the Firebase consent settings. */
public class ApplyConsentUseCase {
    private final SettingsRepository repository;

    public ApplyConsentUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public void invoke() {
        repository.applyConsent();
    }
}
