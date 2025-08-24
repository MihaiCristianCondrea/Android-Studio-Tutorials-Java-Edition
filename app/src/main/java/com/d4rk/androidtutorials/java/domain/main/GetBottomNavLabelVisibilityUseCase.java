package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.ui.screens.main.repository.MainRepository;

/** Returns bottom navigation label visibility preference. */
public class GetBottomNavLabelVisibilityUseCase {
    private final MainRepository repository;

    public GetBottomNavLabelVisibilityUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public String invoke(String key, String defaultValue) {
        return repository.getBottomNavLabelVisibility(key, defaultValue);
    }
}
