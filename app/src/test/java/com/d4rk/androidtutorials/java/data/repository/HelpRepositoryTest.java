package com.d4rk.androidtutorials.java.data.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;

import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class HelpRepositoryTest {

    @SuppressWarnings("unchecked")
    @Test
    public void requestReviewFlow_notifiesSuccessListener() {
        Context context = mock(Context.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        Task<ReviewInfo> task = mock(Task.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);
        com.d4rk.androidtutorials.java.data.repository.HelpRepository.OnReviewInfoListener listener =
                mock(com.d4rk.androidtutorials.java.data.repository.HelpRepository.OnReviewInfoListener.class);

        try (MockedStatic<ReviewManagerFactory> reviewManagerFactoryMockedStatic = Mockito.mockStatic(ReviewManagerFactory.class)) {
            reviewManagerFactoryMockedStatic.when(() -> ReviewManagerFactory.create(context)).thenReturn(reviewManager);
            when(reviewManager.requestReviewFlow()).thenReturn(task);
            when(task.addOnSuccessListener(any(OnSuccessListener.class))).thenAnswer(invocation -> {
                OnSuccessListener<ReviewInfo> onSuccessListener = invocation.getArgument(0);
                onSuccessListener.onSuccess(reviewInfo);
                return task;
            });
            when(task.addOnFailureListener(any(OnFailureListener.class))).thenReturn(task);

            HelpRepository repository = new HelpRepository(context);

            repository.requestReviewFlow(listener);

            verify(listener).onSuccess(reviewInfo);
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void requestReviewFlow_notifiesFailureListener() {
        Context context = mock(Context.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        Task<ReviewInfo> task = mock(Task.class);
        Exception error = new Exception("failure");
        com.d4rk.androidtutorials.java.data.repository.HelpRepository.OnReviewInfoListener listener =
                mock(com.d4rk.androidtutorials.java.data.repository.HelpRepository.OnReviewInfoListener.class);

        try (MockedStatic<ReviewManagerFactory> reviewManagerFactoryMockedStatic = Mockito.mockStatic(ReviewManagerFactory.class)) {
            reviewManagerFactoryMockedStatic.when(() -> ReviewManagerFactory.create(context)).thenReturn(reviewManager);
            when(reviewManager.requestReviewFlow()).thenReturn(task);
            when(task.addOnSuccessListener(any(OnSuccessListener.class))).thenReturn(task);
            when(task.addOnFailureListener(any(OnFailureListener.class))).thenAnswer(invocation -> {
                OnFailureListener onFailureListener = invocation.getArgument(0);
                onFailureListener.onFailure(error);
                return task;
            });

            HelpRepository repository = new HelpRepository(context);

            repository.requestReviewFlow(listener);

            verify(listener).onFailure(error);
        }
    }

    @Test
    public void launchReviewFlow_forwardsToManager() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        ReviewManager reviewManager = mock(ReviewManager.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);

        try (MockedStatic<ReviewManagerFactory> reviewManagerFactoryMockedStatic = Mockito.mockStatic(ReviewManagerFactory.class)) {
            reviewManagerFactoryMockedStatic.when(() -> ReviewManagerFactory.create(context)).thenReturn(reviewManager);
            HelpRepository repository = new HelpRepository(context);

            repository.launchReviewFlow(activity, reviewInfo);

            verify(reviewManager).launchReviewFlow(activity, reviewInfo);
        }
    }
}
