package com.d4rk.androidtutorials.java.ui.screens.help;


import android.app.Activity;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.domain.help.LaunchReviewFlowUseCase;
import com.d4rk.androidtutorials.java.domain.help.RequestReviewFlowUseCase;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.google.android.play.core.review.ReviewInfo;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the Help screen. Delegates to HelpRepository for
 * requesting or launching in-app reviews.
 */
@HiltViewModel
public class HelpViewModel extends ViewModel {

    private final RequestReviewFlowUseCase requestReviewFlowUseCase;
    private final LaunchReviewFlowUseCase launchReviewFlowUseCase;

    @Inject
    public HelpViewModel(RequestReviewFlowUseCase requestReviewFlowUseCase,
                         LaunchReviewFlowUseCase launchReviewFlowUseCase) {
        this.requestReviewFlowUseCase = requestReviewFlowUseCase;
        this.launchReviewFlowUseCase = launchReviewFlowUseCase;
    }

    /**
     * Requests a review flow. On success, you get a ReviewInfo.
     * On failure, handle the exception (like fallback to the Play Store).
     */
    public void requestReviewFlow(HelpRepository.OnReviewInfoListener listener) {
        requestReviewFlowUseCase.invoke(listener);
    }

    /**
     * Launches the in-app review flow with the given ReviewInfo.
     */
    public void launchReviewFlow(Activity activity, ReviewInfo info) {
        launchReviewFlowUseCase.invoke(activity, info);
    }
}