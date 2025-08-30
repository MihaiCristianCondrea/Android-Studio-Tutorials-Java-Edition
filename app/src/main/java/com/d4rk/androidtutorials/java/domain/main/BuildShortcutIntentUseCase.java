package com.d4rk.androidtutorials.java.domain.main;

import android.content.Intent;
import com.d4rk.androidtutorials.java.data.repository.MainRepository;

/** Creates an intent for the app shortcut. */
public class BuildShortcutIntentUseCase {
    private final MainRepository repository;

    public BuildShortcutIntentUseCase(MainRepository repository) {
        this.repository = repository;
    }

    public Intent invoke(boolean isInstalled) {
        return repository.buildShortcutIntent(isInstalled);
    }
}
