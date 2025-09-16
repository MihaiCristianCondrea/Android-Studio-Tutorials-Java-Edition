package com.d4rk.androidtutorials.java.utils;

import android.app.Activity;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

/**
 * Utility class for launching Google Play in-app reviews.
 */
public final class ReviewHelper {

    private ReviewHelper() {
        // Utility class
    }

    public static void launchInAppReviewIfEligible(Activity activity,
                                                   int sessionCount,
                                                   boolean hasPromptedBefore,
                                                   Runnable onReviewLaunched) {
        if (sessionCount < 3 || hasPromptedBefore) {
            return;
        }
        launchReview(activity, onReviewLaunched);
    }

    public static void forceLaunchInAppReview(Activity activity) {
        launchReview(activity, null);
    }

    private static void launchReview(Activity activity, Runnable onReviewLaunched) {
        ReviewManager reviewManager = ReviewManagerFactory.create(activity);
        reviewManager.requestReviewFlow()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReviewInfo reviewInfo = task.getResult();
                        reviewManager.launchReviewFlow(activity, reviewInfo)
                                .addOnCompleteListener(flow -> {
                                    if (onReviewLaunched != null) {
                                        onReviewLaunched.run();
                                    }
                                });
                    }
                });
    }
}
