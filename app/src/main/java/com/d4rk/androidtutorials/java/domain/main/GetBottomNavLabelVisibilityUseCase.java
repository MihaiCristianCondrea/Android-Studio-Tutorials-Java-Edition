package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/** Returns bottom navigation label visibility preference. */
public class GetBottomNavLabelVisibilityUseCase {
    private final MainRepository repository;

    public GetBottomNavLabelVisibilityUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public String invoke() {
        return repository.getBottomNavLabelVisibility();
    }
}
