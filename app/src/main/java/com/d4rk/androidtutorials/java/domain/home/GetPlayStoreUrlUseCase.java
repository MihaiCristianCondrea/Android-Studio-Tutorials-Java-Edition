package com.d4rk.androidtutorials.java.domain.home;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

/** Use case that provides the Play Store URL for this app. */
public class GetPlayStoreUrlUseCase {
    private final HomeRepository repository;

    public GetPlayStoreUrlUseCase(HomeRepository repository) {
        this.repository = repository;
    }

    /** Returns the Play Store URL for the application. */
    public String invoke() {
        return repository.getPlayStoreUrl();
    }
}
