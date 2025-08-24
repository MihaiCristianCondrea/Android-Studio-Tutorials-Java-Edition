package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.ui.screens.main.repository.MainRepository;

/** Returns the default tab preference string. */
public class GetDefaultTabPreferenceUseCase {
    private final MainRepository repository;

    public GetDefaultTabPreferenceUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public String invoke(String key, String defaultValue) {
        return repository.getDefaultTabPreference(key, defaultValue);
    }
}
