package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.ui.screens.main.repository.MainRepository;

/** Determines if the startup screen should be shown. */
public class ShouldShowStartupScreenUseCase {
    private final MainRepository repository;

    public ShouldShowStartupScreenUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public boolean invoke() {
        return repository.shouldShowStartupScreen();
    }
}
