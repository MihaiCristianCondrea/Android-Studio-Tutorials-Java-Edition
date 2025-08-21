package com.d4rk.androidtutorials.java.domain.main;

import com.d4rk.androidtutorials.java.ui.screens.main.repository.MainRepository;
import com.google.android.play.core.appupdate.AppUpdateManager;

/** Provides the AppUpdateManager instance. */
public class GetAppUpdateManagerUseCase {
    private final MainRepository repository;

    public GetAppUpdateManagerUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public AppUpdateManager invoke() {
        return repository.getAppUpdateManager();
    }
}
