package com.d4rk.androidtutorials.java.ui.screens.help;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Activity;

import com.d4rk.androidtutorials.java.domain.help.LaunchReviewFlowUseCase;
import com.d4rk.androidtutorials.java.domain.help.RequestReviewFlowUseCase;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.google.android.play.core.review.ReviewInfo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HelpViewModelTest {

    @Mock
    private RequestReviewFlowUseCase requestReviewFlowUseCase;

    @Mock
    private LaunchReviewFlowUseCase launchReviewFlowUseCase;

    @Test
    public void requestReviewFlow_delegatesToUseCase() {
        HelpViewModel viewModel = new HelpViewModel(requestReviewFlowUseCase, launchReviewFlowUseCase);
        HelpRepository.OnReviewInfoListener listener = mock(HelpRepository.OnReviewInfoListener.class);

        viewModel.requestReviewFlow(listener);

        verify(requestReviewFlowUseCase).invoke(listener);
    }

    @Test
    public void requestReviewFlow_propagatesFailure() {
        doThrow(new IllegalStateException("error"))
                .when(requestReviewFlowUseCase)
                .invoke(any());

        HelpViewModel viewModel = new HelpViewModel(requestReviewFlowUseCase, launchReviewFlowUseCase);

        HelpRepository.OnReviewInfoListener listener = mock(HelpRepository.OnReviewInfoListener.class);

        assertThrows(IllegalStateException.class, () -> viewModel.requestReviewFlow(listener));
    }

    @Test
    public void launchReviewFlow_delegatesToUseCase() {
        HelpViewModel viewModel = new HelpViewModel(requestReviewFlowUseCase, launchReviewFlowUseCase);

        Activity activity = mock(Activity.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);

        viewModel.launchReviewFlow(activity, reviewInfo);

        verify(launchReviewFlowUseCase).invoke(activity, reviewInfo);
    }

    @Test
    public void launchReviewFlow_propagatesFailure() {
        Activity activity = mock(Activity.class);
        ReviewInfo reviewInfo = mock(ReviewInfo.class);

        doThrow(new RuntimeException("boom"))
                .when(launchReviewFlowUseCase)
                .invoke(activity, reviewInfo);

        HelpViewModel viewModel = new HelpViewModel(requestReviewFlowUseCase, launchReviewFlowUseCase);

        assertThrows(RuntimeException.class, () -> viewModel.launchReviewFlow(activity, reviewInfo));
    }
}

