package com.d4rk.androidtutorials.java.utils;

import android.app.Activity;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import org.junit.Test;
import org.mockito.MockedStatic;

import static org.mockito.Mockito.*;

/**
 * Tests for {@link ReviewHelper}.
 */
public class ReviewHelperTest {

    @Test
    public void launchInAppReviewIfEligible_doesNothingWhenSessionCountLessThanThree() {
        Activity activity = mock(Activity.class);
        Runnable callback = mock(Runnable.class);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 2, false, callback);

            factory.verifyNoInteractions();
            verifyNoInteractions(callback);
        }
    }

    @Test
    public void launchInAppReviewIfEligible_doesNothingWhenPromptedBefore() {
        Activity activity = mock(Activity.class);
        Runnable callback = mock(Runnable.class);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 5, true, callback);

            factory.verifyNoInteractions();
            verifyNoInteractions(callback);
        }
    }

    @Test
    public void launchInAppReviewIfEligible_launchesReviewWhenEligible() {
        Activity activity = mock(Activity.class);
        Runnable callback = mock(Runnable.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);

        Task<ReviewInfo> requestTask = Tasks.forResult(reviewInfo);
        Task<Void> launchTask = Tasks.forResult(null);

        when(reviewManager.requestReviewFlow()).thenReturn(requestTask);
        when(reviewManager.launchReviewFlow(activity, reviewInfo)).thenReturn(launchTask);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            factory.when(() -> ReviewManagerFactory.create(activity)).thenReturn(reviewManager);

            ReviewHelper.launchInAppReviewIfEligible(activity, 3, false, callback);

            factory.verify(() -> ReviewManagerFactory.create(activity));
            verify(reviewManager).launchReviewFlow(activity, reviewInfo);
            verify(callback).run();
        }
    }

    @Test
    public void forceLaunchInAppReview_alwaysTriggersLaunch() {
        Activity activity = mock(Activity.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);

        Task<ReviewInfo> requestTask = Tasks.forResult(reviewInfo);
        Task<Void> launchTask = Tasks.forResult(null);

        when(reviewManager.requestReviewFlow()).thenReturn(requestTask);
        when(reviewManager.launchReviewFlow(activity, reviewInfo)).thenReturn(launchTask);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            factory.when(() -> ReviewManagerFactory.create(activity)).thenReturn(reviewManager);

            ReviewHelper.forceLaunchInAppReview(activity);

            factory.verify(() -> ReviewManagerFactory.create(activity));
            verify(reviewManager).launchReviewFlow(activity, reviewInfo);
        }
    }
}

