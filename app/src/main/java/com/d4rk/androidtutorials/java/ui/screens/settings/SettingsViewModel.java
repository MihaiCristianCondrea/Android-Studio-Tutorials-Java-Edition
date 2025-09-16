package com.d4rk.androidtutorials.java.ui.screens.settings;

import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.domain.settings.ApplyConsentUseCase;
import com.d4rk.androidtutorials.java.domain.settings.GetDarkModeUseCase;
import com.d4rk.androidtutorials.java.domain.settings.OnPreferenceChangedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.RegisterPreferenceChangeListenerUseCase;
import com.d4rk.androidtutorials.java.domain.settings.SetConsentAcceptedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.UnregisterPreferenceChangeListenerUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


/**
 * ViewModel for the Settings screen. Delegates to SettingsRepository for
 * reading/writing preferences, applying theme, etc.
 */
@HiltViewModel
public class SettingsViewModel extends ViewModel {

    private final OnPreferenceChangedUseCase onPreferenceChangedUseCase;
    private final RegisterPreferenceChangeListenerUseCase registerPreferenceChangeListenerUseCase;
    private final UnregisterPreferenceChangeListenerUseCase unregisterPreferenceChangeListenerUseCase;
    private final GetDarkModeUseCase getDarkModeUseCase;
    private final SetConsentAcceptedUseCase setConsentAcceptedUseCase;
    private final ApplyConsentUseCase applyConsentUseCase;

    @Inject
    public SettingsViewModel(OnPreferenceChangedUseCase onPreferenceChangedUseCase,
                             RegisterPreferenceChangeListenerUseCase registerPreferenceChangeListenerUseCase,
                             UnregisterPreferenceChangeListenerUseCase unregisterPreferenceChangeListenerUseCase,
                             GetDarkModeUseCase getDarkModeUseCase,
                             SetConsentAcceptedUseCase setConsentAcceptedUseCase,
                             ApplyConsentUseCase applyConsentUseCase) {
        this.onPreferenceChangedUseCase = onPreferenceChangedUseCase;
        this.registerPreferenceChangeListenerUseCase = registerPreferenceChangeListenerUseCase;
        this.unregisterPreferenceChangeListenerUseCase = unregisterPreferenceChangeListenerUseCase;
        this.getDarkModeUseCase = getDarkModeUseCase;
        this.setConsentAcceptedUseCase = setConsentAcceptedUseCase;
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

    public void applyConsent() {
        applyConsentUseCase.invoke();
    }

    public void registerPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        registerPreferenceChangeListenerUseCase.invoke(listener);
    }

    public void unregisterPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        unregisterPreferenceChangeListenerUseCase.invoke(listener);
    }

    public String getDarkMode() {
        return getDarkModeUseCase.invoke();
    }

    public void setConsentAccepted(boolean accepted) { // FIXME: Method 'setConsentAccepted(boolean)' is never used
        setConsentAcceptedUseCase.invoke(accepted);
    }
}