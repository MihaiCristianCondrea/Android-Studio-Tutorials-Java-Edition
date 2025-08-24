package com.d4rk.androidtutorials.java.ui.screens.startup;


import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.d4rk.androidtutorials.java.ui.screens.startup.repository.StartupRepository;
import com.d4rk.androidtutorials.java.domain.startup.RequestConsentInfoUseCase;
import com.d4rk.androidtutorials.java.domain.startup.LoadConsentFormUseCase;
import com.google.android.ump.ConsentRequestParameters;

/**
 * ViewModel for the startup screen.
 * Handles consent logic by delegating to StartupRepository.
 */
public class StartupViewModel extends AndroidViewModel {

    private final RequestConsentInfoUseCase requestConsentInfoUseCase;
    private final LoadConsentFormUseCase loadConsentFormUseCase;

    public StartupViewModel(@NonNull Application application) {
        super(application);
        StartupRepository repository = new StartupRepository(application);
        requestConsentInfoUseCase = new RequestConsentInfoUseCase(repository);
        loadConsentFormUseCase = new LoadConsentFormUseCase(repository);
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