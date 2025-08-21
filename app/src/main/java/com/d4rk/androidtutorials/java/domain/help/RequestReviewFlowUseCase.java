package com.d4rk.androidtutorials.java.domain.help;

import android.app.Activity;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;

/** Requests the Google Play review flow. */
public class RequestReviewFlowUseCase {
    private final HelpRepository repository;

    public RequestReviewFlowUseCase(HelpRepository repository) {
        this.repository = repository;
    }

    public void invoke(Activity activity, HelpRepository.OnReviewInfoListener listener) {
        repository.requestReviewFlow(activity, listener);
    }
}
