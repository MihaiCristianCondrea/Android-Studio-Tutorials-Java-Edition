package com.d4rk.androidtutorials.java.domain.startup;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.StartupRepository;

/** Loads and shows the consent form if required. */
public class LoadConsentFormUseCase {
    private final StartupRepository repository;

    public LoadConsentFormUseCase(StartupRepository repository) {
        this.repository = repository;
    }

    public void invoke(Activity activity, StartupRepository.OnFormError onError) {
        repository.loadConsentForm(activity, onError);
    }
}
