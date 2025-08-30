package com.d4rk.androidtutorials.java.domain.settings;

import android.content.SharedPreferences;
import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

/** Provides shared preferences used by the settings screen. */
public class GetSharedPreferencesUseCase {
    private final SettingsRepository repository;

    public GetSharedPreferencesUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public SharedPreferences invoke() {
        return repository.getSharedPreferences();
    }
}
