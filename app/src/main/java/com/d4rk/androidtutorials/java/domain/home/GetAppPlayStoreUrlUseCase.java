package com.d4rk.androidtutorials.java.domain.home;

import com.d4rk.androidtutorials.java.data.repository.HomeRepository;

/**
 * Use case that builds a Play Store URL for a given package name.
 */
public class GetAppPlayStoreUrlUseCase {
    private final HomeRepository repository;

    public GetAppPlayStoreUrlUseCase(HomeRepository repository) {
        this.repository = repository;
    }

    /**
     * Returns the Play Store URL for the specified package name.
     */
    public String invoke(String packageName) {
        return repository.getAppPlayStoreUrl(packageName);
    }
}
