package com.d4rk.androidtutorials.java.ui.screens.settings;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.domain.settings.OnPreferenceChangedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.GetSharedPreferencesUseCase;
import com.d4rk.androidtutorials.java.domain.settings.ApplyConsentUseCase;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;


/**
 * ViewModel for the Settings screen. Delegates to SettingsRepository for
 * reading/writing preferences, applying theme, etc.
 */
@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final OnPreferenceChangedUseCase onPreferenceChangedUseCase;
    private final GetSharedPreferencesUseCase getSharedPreferencesUseCase;
    private final ApplyConsentUseCase applyConsentUseCase;

    @Inject
    public SettingsViewModel(OnPreferenceChangedUseCase onPreferenceChangedUseCase,
                             GetSharedPreferencesUseCase getSharedPreferencesUseCase,
                             ApplyConsentUseCase applyConsentUseCase) {
        this.onPreferenceChangedUseCase = onPreferenceChangedUseCase;
        this.getSharedPreferencesUseCase = getSharedPreferencesUseCase;
        this.applyConsentUseCase = applyConsentUseCase;
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