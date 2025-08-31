package com.d4rk.androidtutorials.java.domain.settings;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

/** Returns the stored dark mode preference value. */
public class GetDarkModeUseCase {
    private final SettingsRepository repository;

    public GetDarkModeUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public String invoke() {
        return repository.getDarkMode();
    }
}
