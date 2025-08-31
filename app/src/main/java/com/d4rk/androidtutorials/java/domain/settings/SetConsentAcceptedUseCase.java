package com.d4rk.androidtutorials.java.domain.settings;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

/** Writes whether the user has accepted analytics consent. */
public class SetConsentAcceptedUseCase {
    private final SettingsRepository repository;

    public SetConsentAcceptedUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public void invoke(boolean accepted) {
        repository.setConsentAccepted(accepted);
    }
}
