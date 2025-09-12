package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/**
 * Applies theme preference and returns true if changed.
 */
public class ApplyThemeSettingsUseCase {
    private final MainRepository repository;

    public ApplyThemeSettingsUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public boolean invoke(String[] darkModeValues) {
        return repository.applyThemeSettings(darkModeValues);
    }
}
