package com.d4rk.androidtutorials.java.ui.screens.help.repository;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

/**
 * Repository for the Help screen. Manages the ReviewManager and in-app review flow.
 */
public class HelpRepository implements com.d4rk.androidtutorials.java.data.repository.HelpRepository {

    private final ReviewManager reviewManager;

    public HelpRepository(@NonNull Context context) {
        this.reviewManager = ReviewManagerFactory.create(context);
    }

    /**
     * Requests the review flow from Google Play.
     * onSuccess -> returns the ReviewInfo to the caller
     * onFailure -> callback with exception
     */
    public void requestReviewFlow(@NonNull com.d4rk.androidtutorials.java.data.repository.HelpRepository.OnReviewInfoListener listener) {
        reviewManager.requestReviewFlow()
                .addOnSuccessListener(listener::onSuccess)
                .addOnFailureListener(listener::onFailure);
    }

    /**
     * Launches the review flow with the provided ReviewInfo.
     * You can add a success/failure callback if desired.
     */
    public void launchReviewFlow(
            @NonNull Activity activity,
            @NonNull ReviewInfo reviewInfo
    ) {
        reviewManager.launchReviewFlow(activity, reviewInfo);
    }

}