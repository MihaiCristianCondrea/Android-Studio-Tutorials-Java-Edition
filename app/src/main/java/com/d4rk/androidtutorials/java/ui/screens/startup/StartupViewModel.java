package com.d4rk.androidtutorials.java.ui.screens.startup;


import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.domain.startup.LoadConsentFormUseCase;
import com.d4rk.androidtutorials.java.domain.startup.RequestConsentInfoUseCase;
import com.d4rk.androidtutorials.java.ui.screens.startup.repository.StartupRepository;
import com.google.android.ump.ConsentRequestParameters;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the startup screen.
 * Handles consent logic by delegating to StartupRepository.
 */
@HiltViewModel
public class StartupViewModel extends ViewModel {

    private final RequestConsentInfoUseCase requestConsentInfoUseCase;
    private final LoadConsentFormUseCase loadConsentFormUseCase;

    @Inject
    public StartupViewModel(RequestConsentInfoUseCase requestConsentInfoUseCase,
                            LoadConsentFormUseCase loadConsentFormUseCase) {
        this.requestConsentInfoUseCase = requestConsentInfoUseCase;
        this.loadConsentFormUseCase = loadConsentFormUseCase;
    }

    /**
     * Request the consent info update.
     */
    public void requestConsentInfoUpdate(Activity activity,
                                         ConsentRequestParameters params,
                                         Runnable onSuccess,
                                         StartupRepository.OnFormError onError) {
        requestConsentInfoUseCase.invoke(activity, params, onSuccess, onError);
    }

    /**
     * Load the consent form (and show it if required).
     */
    public void loadConsentForm(Activity activity, StartupRepository.OnFormError onError) {
        loadConsentFormUseCase.invoke(activity, onError);
    }
}