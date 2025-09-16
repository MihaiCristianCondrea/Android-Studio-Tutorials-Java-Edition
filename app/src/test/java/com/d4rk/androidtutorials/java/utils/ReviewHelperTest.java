package com.d4rk.androidtutorials.java.utils;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import android.app.Activity;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.Task;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;

public class ReviewHelperTest {

    @Test
    public void launchInAppReviewIfEligible_whenSessionCountTooLow_doesNotStartFlow() {
        Activity activity = mock(Activity.class);
        Runnable onReviewLaunched = mock(Runnable.class);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 1, false, onReviewLaunched);

            factory.verifyNoInteractions();
        }

        verifyNoInteractions(onReviewLaunched);
    }

    @Test
    public void launchInAppReviewIfEligible_whenAlreadyPrompted_doesNotStartFlow() {
        Activity activity = mock(Activity.class);
        Runnable onReviewLaunched = mock(Runnable.class);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 5, true, onReviewLaunched);

            factory.verifyNoInteractions();
        }

        verifyNoInteractions(onReviewLaunched);
    }

    @Test
    public void launchInAppReviewIfEligible_whenEligible_launchesReviewAndRunsCallback() {
        Activity activity = mock(Activity.class);
        Runnable onReviewLaunched = mock(Runnable.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);
        Task<ReviewInfo> requestTask = mock(Task.class);
        Task<Void> launchTask = mock(Task.class);

        try (MockedStatic<ReviewManagerFactory> factory = stubSuccessfulReviewFlow(activity, reviewManager, requestTask, launchTask, reviewInfo)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 3, false, onReviewLaunched);

            factory.verify(() -> ReviewManagerFactory.create(activity));
        }

        verify(reviewManager).requestReviewFlow();
        verify(reviewManager).launchReviewFlow(activity, reviewInfo);
        verify(onReviewLaunched).run();
    }

    @Test
    public void forceLaunchInAppReview_alwaysRequestsReviewFlow() {
        Activity activity = mock(Activity.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);
        Task<ReviewInfo> requestTask = mock(Task.class);
        Task<Void> launchTask = mock(Task.class);

        try (MockedStatic<ReviewManagerFactory> factory = stubSuccessfulReviewFlow(activity, reviewManager, requestTask, launchTask, reviewInfo)) {
            ReviewHelper.forceLaunchInAppReview(activity);

            factory.verify(() -> ReviewManagerFactory.create(activity));
        }

        verify(reviewManager).requestReviewFlow();
        verify(reviewManager).launchReviewFlow(activity, reviewInfo);
    }

    @SuppressWarnings("unchecked")
    private MockedStatic<ReviewManagerFactory> stubSuccessfulReviewFlow(
            Activity activity,
            ReviewManager reviewManager,
            Task<ReviewInfo> requestTask,
            Task<Void> launchTask,
            ReviewInfo reviewInfo
    ) {
        MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class);
        factory.when(() -> ReviewManagerFactory.create(activity)).thenReturn(reviewManager);

        when(reviewManager.requestReviewFlow()).thenReturn(requestTask);
        when(requestTask.isSuccessful()).thenReturn(true);
        when(requestTask.getResult()).thenReturn(reviewInfo);
        when(reviewManager.launchReviewFlow(activity, reviewInfo)).thenReturn(launchTask);

        doAnswer(invocation -> {
            OnCompleteListener<ReviewInfo> listener = invocation.getArgument(0);
            listener.onComplete(requestTask);
            return requestTask;
        }).when(requestTask).addOnCompleteListener(ArgumentMatchers.<OnCompleteListener<ReviewInfo>>any());

        doAnswer(invocation -> {
            OnCompleteListener<Void> listener = invocation.getArgument(0);
            listener.onComplete(launchTask);
            return launchTask;
        }).when(launchTask).addOnCompleteListener(ArgumentMatchers.<OnCompleteListener<Void>>any());

        return factory;
    }
}

