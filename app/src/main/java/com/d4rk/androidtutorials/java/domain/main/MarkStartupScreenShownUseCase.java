package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/** Marks that the startup screen has been shown. */
public class MarkStartupScreenShownUseCase {
    private final MainRepository repository;

    public MarkStartupScreenShownUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public void invoke() {
        repository.markStartupScreenShown();
    }
}
