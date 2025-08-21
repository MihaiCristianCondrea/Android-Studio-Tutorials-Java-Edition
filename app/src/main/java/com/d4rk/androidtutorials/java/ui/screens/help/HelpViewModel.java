package com.d4rk.androidtutorials.java.ui.screens.help;


import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.d4rk.androidtutorials.java.domain.help.RequestReviewFlowUseCase;
import com.d4rk.androidtutorials.java.domain.help.LaunchReviewFlowUseCase;
import com.google.android.play.core.review.ReviewInfo;

/**
 * ViewModel for the Help screen. Delegates to HelpRepository for
 * requesting or launching in-app reviews.
 */
public class HelpViewModel extends AndroidViewModel {

    private final RequestReviewFlowUseCase requestReviewFlowUseCase;
    private final LaunchReviewFlowUseCase launchReviewFlowUseCase;

    public HelpViewModel(@NonNull Application application) {
        super(application);
        HelpRepository repository = new HelpRepository(application);
        requestReviewFlowUseCase = new RequestReviewFlowUseCase(repository);
        launchReviewFlowUseCase = new LaunchReviewFlowUseCase(repository);
    }

    /**
     * Requests a review flow. On success, you get a ReviewInfo.
     * On failure, handle the exception (like fallback to the Play Store).
     */
    public void requestReviewFlow(Activity activity, HelpRepository.OnReviewInfoListener listener) {
        requestReviewFlowUseCase.invoke(activity, listener);
    }

    /**
     * Launches the in-app review flow with the given ReviewInfo.
     */
    public void launchReviewFlow(Activity activity, ReviewInfo info) {
        launchReviewFlowUseCase.invoke(activity, info);
    }
}