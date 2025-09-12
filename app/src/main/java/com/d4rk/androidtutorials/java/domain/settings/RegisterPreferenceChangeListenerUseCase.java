package com.d4rk.androidtutorials.java.domain.settings;

import android.content.SharedPreferences;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

/** Registers a listener for preference changes. */
public class RegisterPreferenceChangeListenerUseCase {
    private final SettingsRepository repository;

    public RegisterPreferenceChangeListenerUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public void invoke(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        repository.registerPreferenceChangeListener(listener);
    }
}
