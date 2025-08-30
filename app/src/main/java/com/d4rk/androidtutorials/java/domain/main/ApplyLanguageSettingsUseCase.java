package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/** Applies the saved language preference. */
public class ApplyLanguageSettingsUseCase {
    private final MainRepository repository;

    public ApplyLanguageSettingsUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public void invoke() {
        repository.applyLanguageSettings();
    }
}
