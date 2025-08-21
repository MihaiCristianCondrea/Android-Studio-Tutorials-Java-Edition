package com.d4rk.androidtutorials.java.domain.main;

import android.content.pm.PackageManager;
import com.d4rk.androidtutorials.java.ui.screens.main.repository.MainRepository;

/** Checks if an app is installed by package name. */
public class IsAppInstalledUseCase {
    private final MainRepository repository;

    public IsAppInstalledUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public boolean invoke(PackageManager pm, String packageName) {
        return repository.isAppInstalled(pm, packageName);
    }
}
