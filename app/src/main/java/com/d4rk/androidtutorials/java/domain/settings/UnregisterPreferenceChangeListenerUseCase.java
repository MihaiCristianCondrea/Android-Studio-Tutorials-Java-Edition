package com.d4rk.androidtutorials.java.domain.settings;

import android.content.SharedPreferences;
import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

/** Unregisters a listener for preference changes. */
public class UnregisterPreferenceChangeListenerUseCase {
    private final SettingsRepository repository;

    public UnregisterPreferenceChangeListenerUseCase(SettingsRepository repository) {
        this.repository = repository;
    }

    public void invoke(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        repository.unregisterPreferenceChangeListener(listener);
    }
}
