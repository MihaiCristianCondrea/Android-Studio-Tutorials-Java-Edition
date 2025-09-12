package com.d4rk.androidtutorials.java.domain.startup;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.StartupRepository;
import com.google.android.ump.ConsentRequestParameters;

/**
 * Requests consent info update via UMP.
 */
public class RequestConsentInfoUseCase {
    private final StartupRepository repository;

    public RequestConsentInfoUseCase(StartupRepository repository) {
        this.repository = repository;
    }

    public void invoke(Activity activity, ConsentRequestParameters params, Runnable onSuccess, StartupRepository.OnFormError onError) {
        repository.requestConsentInfoUpdate(activity, params, onSuccess, onError);
    }
}
