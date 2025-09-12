package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/**
 * Returns the default tab preference string.
 */
public class GetDefaultTabPreferenceUseCase {
    private final MainRepository repository;

    public GetDefaultTabPreferenceUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public String invoke() {
        return repository.getDefaultTabPreference();
    }
}
