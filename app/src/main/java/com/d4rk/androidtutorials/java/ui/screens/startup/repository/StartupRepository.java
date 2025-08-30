package com.d4rk.androidtutorials.java.ui.screens.startup.repository;

import android.app.Activity;
import android.content.Context;

import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

/**
 * Repository that handles consent logic for the startup screen.
 * It keeps references to ConsentInformation and ConsentForm.
 */
public class StartupRepository implements com.d4rk.androidtutorials.java.data.repository.StartupRepository {

    private final ConsentInformation consentInformation;
    private ConsentForm consentForm;

    public StartupRepository(Context context) {
        consentInformation = UserMessagingPlatform.getConsentInformation(context);
    }

    /**
     * Requests consent info update from the UMP SDK.
     *
     * @param activity  the current Activity
     * @param params    the ConsentRequestParameters
     * @param onSuccess callback invoked when request succeeds
     * @param onError   callback invoked on failure
     */
    public void requestConsentInfoUpdate(Activity activity,
                                         ConsentRequestParameters params,
                                         Runnable onSuccess,
                                         com.d4rk.androidtutorials.java.data.repository.StartupRepository.OnFormError onError) {
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                () -> {
                    consentInformation.isConsentFormAvailable();
                    onSuccess.run();
                },
                formError -> {
                    if (onError != null) {
                        onError.onFormError(formError);
                    }
                }
        );
    }

    /**
     * Loads the consent form and shows it if required.
     *
     * @param activity the current Activity
     * @param onError  callback invoked if there's a problem loading or showing the form
     */
    public void loadConsentForm(Activity activity, com.d4rk.androidtutorials.java.data.repository.StartupRepository.OnFormError onError) {
        UserMessagingPlatform.loadConsentForm(
                activity,
                form -> {
                    this.consentForm = form;
                    if (consentInformation.getConsentStatus()
                            == ConsentInformation.ConsentStatus.REQUIRED) {
                        consentForm.show(
                                activity,
                                formError -> loadConsentForm(activity, onError)
                        );
                    }
                },
                formError -> {
                    if (onError != null) {
                        onError.onFormError(formError);
                    }
                }
        );
    }

}