package com.d4rk.androidtutorials.java.utils;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import android.app.Activity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockedStatic;

public class ReviewHelperTest {

    @Test
    public void launchInAppReviewIfEligible_launchesReviewWhenEligible() {
        Activity activity = mock(Activity.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        Task<ReviewInfo> reviewInfoTask = mock(Task.class);
        Task<Void> flowTask = mock(Task.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);
        Runnable callback = mock(Runnable.class);

        when(reviewManager.requestReviewFlow()).thenReturn(reviewInfoTask);
        when(reviewInfoTask.isSuccessful()).thenReturn(true);
        when(reviewInfoTask.getResult()).thenReturn(reviewInfo);
        when(reviewManager.launchReviewFlow(activity, reviewInfo)).thenReturn(flowTask);

        doAnswer(invocation -> {
            OnCompleteListener<ReviewInfo> listener = invocation.getArgument(0);
            listener.onComplete(reviewInfoTask);
            return reviewInfoTask;
        }).when(reviewInfoTask).addOnCompleteListener(ArgumentMatchers.<OnCompleteListener<ReviewInfo>>any());

        doAnswer(invocation -> {
            OnCompleteListener<Void> listener = invocation.getArgument(0);
            listener.onComplete(flowTask);
            return flowTask;
        }).when(flowTask).addOnCompleteListener(ArgumentMatchers.<OnCompleteListener<Void>>any());

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            factory.when(() -> ReviewManagerFactory.create(activity)).thenReturn(reviewManager);

            ReviewHelper.launchInAppReviewIfEligible(activity, 3, false, callback);

            factory.verify(times(1), () -> ReviewManagerFactory.create(activity));
        }

        verify(reviewManager).requestReviewFlow();
        verify(reviewManager).launchReviewFlow(activity, reviewInfo);
        verify(callback, times(1)).run();
    }

    @Test
    public void launchInAppReviewIfEligible_doesNotLaunchWhenBelowThreshold() {
        Activity activity = mock(Activity.class);
        Runnable callback = mock(Runnable.class);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 2, false, callback);

            factory.verifyNoInteractions();
        }

        verifyNoInteractions(callback);
    }

    @Test
    public void launchInAppReviewIfEligible_doesNotLaunchWhenAlreadyPrompted() {
        Activity activity = mock(Activity.class);
        Runnable callback = mock(Runnable.class);

        try (MockedStatic<ReviewManagerFactory> factory = mockStatic(ReviewManagerFactory.class)) {
            ReviewHelper.launchInAppReviewIfEligible(activity, 5, true, callback);

            factory.verifyNoInteractions();
        }

        verifyNoInteractions(callback);
    }
}
