package com.d4rk.androidtutorials.java.ui.screens.settings;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.d4rk.androidtutorials.java.ui.screens.settings.repository.SettingsRepository;
import com.d4rk.androidtutorials.java.domain.settings.OnPreferenceChangedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.GetSharedPreferencesUseCase;
import com.d4rk.androidtutorials.java.domain.settings.ApplyConsentUseCase;


/**
 * ViewModel for the Settings screen. Delegates to SettingsRepository for
 * reading/writing preferences, applying theme, etc.
 */
public class SettingsViewModel extends AndroidViewModel {

    private final SettingsRepository settingsRepository;
    private final OnPreferenceChangedUseCase onPreferenceChangedUseCase;
    private final GetSharedPreferencesUseCase getSharedPreferencesUseCase;
    private final ApplyConsentUseCase applyConsentUseCase;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingsRepository = new SettingsRepository(application);
        onPreferenceChangedUseCase = new OnPreferenceChangedUseCase(settingsRepository);
        getSharedPreferencesUseCase = new GetSharedPreferencesUseCase(settingsRepository);
        applyConsentUseCase = new ApplyConsentUseCase(settingsRepository);
    }

    /**
     * Called by the Activity or Fragment when a preference changes.
     * We do the logic in the repository, and if theme changed, we return true so the UI can recreate.
     */
    public boolean onPreferenceChanged(String key) {
        if (key == null) return false;
        return onPreferenceChangedUseCase.invoke(key);
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferencesUseCase.invoke();
    }

    public void applyConsent() {
        applyConsentUseCase.invoke();
    }
}