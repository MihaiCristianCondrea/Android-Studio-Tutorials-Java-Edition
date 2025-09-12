package com.d4rk.androidtutorials.java.data.repository;

import android.app.Activity;

import com.google.android.play.core.review.ReviewInfo;

public interface HelpRepository {
    void requestReviewFlow(OnReviewInfoListener listener);
    void launchReviewFlow(Activity activity, ReviewInfo reviewInfo);

    interface OnReviewInfoListener {
        void onSuccess(ReviewInfo info);
        void onFailure(Exception e);
    }
}
