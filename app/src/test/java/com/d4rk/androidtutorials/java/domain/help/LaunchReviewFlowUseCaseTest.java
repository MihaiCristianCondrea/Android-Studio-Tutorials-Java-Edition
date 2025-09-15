package com.d4rk.androidtutorials.java.domain.help;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.HelpRepository;
import com.google.android.play.core.review.ReviewInfo;

import org.junit.Test;

public class LaunchReviewFlowUseCaseTest {

    @Test
    public void invokeDelegatesToRepository() {
        HelpRepository repository = mock(HelpRepository.class);
        Activity activity = mock(Activity.class);
        ReviewInfo info = mock(ReviewInfo.class);
        LaunchReviewFlowUseCase useCase = new LaunchReviewFlowUseCase(repository);

        useCase.invoke(activity, info);

        verify(repository).launchReviewFlow(activity, info);
    }

    @Test
    public void invokeAllowsNullActivity() {
        HelpRepository repository = mock(HelpRepository.class);
        ReviewInfo info = mock(ReviewInfo.class);
        LaunchReviewFlowUseCase useCase = new LaunchReviewFlowUseCase(repository);

        useCase.invoke(null, info);

        verify(repository).launchReviewFlow(null, info);
    }

    @Test
    public void invokeAllowsNullReviewInfo() {
        HelpRepository repository = mock(HelpRepository.class);
        Activity activity = mock(Activity.class);
        LaunchReviewFlowUseCase useCase = new LaunchReviewFlowUseCase(repository);

        useCase.invoke(activity, null);

        verify(repository).launchReviewFlow(activity, null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        HelpRepository repository = mock(HelpRepository.class);
        Activity activity = mock(Activity.class);
        ReviewInfo info = mock(ReviewInfo.class);
        LaunchReviewFlowUseCase useCase = new LaunchReviewFlowUseCase(repository);
        doThrow(new IllegalArgumentException("failure"))
                .when(repository).launchReviewFlow(activity, info);

        assertThrows(IllegalArgumentException.class, () -> useCase.invoke(activity, info));
        verify(repository).launchReviewFlow(activity, info);
    }
}
