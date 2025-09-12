package com.d4rk.androidtutorials.java.data.repository;

import android.app.Activity;

import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;

public interface StartupRepository {
    void requestConsentInfoUpdate(
            Activity activity,
            ConsentRequestParameters params,
            Runnable onSuccess,
            OnFormError onError);

    void loadConsentForm(Activity activity, OnFormError onError);

    interface OnFormError {
        void onFormError(FormError error);
    }
}
