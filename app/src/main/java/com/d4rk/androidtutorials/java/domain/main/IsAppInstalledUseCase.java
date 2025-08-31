package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/** Checks if an app is installed by package name. */
public class IsAppInstalledUseCase {
    private final MainRepository repository;

    public IsAppInstalledUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public boolean invoke(String packageName) {
        return repository.isAppInstalled(packageName);
    }
}
