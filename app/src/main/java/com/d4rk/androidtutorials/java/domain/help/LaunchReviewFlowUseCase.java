package com.d4rk.androidtutorials.java.domain.help;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.HelpRepository;
import com.google.android.play.core.review.ReviewInfo;

/** Launches the in-app review flow. */
public class LaunchReviewFlowUseCase {
    private final HelpRepository repository;

    public LaunchReviewFlowUseCase(HelpRepository repository) {
        this.repository = repository;
    }

    public void invoke(Activity activity, ReviewInfo info) {
        repository.launchReviewFlow(activity, info);
    }
}
