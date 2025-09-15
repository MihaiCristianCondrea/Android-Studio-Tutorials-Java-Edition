package com.d4rk.androidtutorials.java.ui.screens.help;

import static org.junit.Assert.assertSame;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.HelpRepository;
import com.d4rk.androidtutorials.java.domain.help.LaunchReviewFlowUseCase;
import com.d4rk.androidtutorials.java.domain.help.RequestReviewFlowUseCase;
import com.google.android.play.core.review.ReviewInfo;

import org.junit.Test;
import org.mockito.Mockito;

public class HelpViewModelTest {

    @Test
    public void requestReviewFlowDelegatesToRepository() {
        FakeHelpRepository repository = new FakeHelpRepository();
        HelpViewModel viewModel = new HelpViewModel(
                new RequestReviewFlowUseCase(repository),
                new LaunchReviewFlowUseCase(repository)
        );
        HelpRepository.OnReviewInfoListener listener = Mockito.mock(HelpRepository.OnReviewInfoListener.class);

        viewModel.requestReviewFlow(listener);

        assertSame(listener, repository.lastListener);
    }

    @Test
    public void launchReviewFlowDelegatesToRepository() {
        FakeHelpRepository repository = new FakeHelpRepository();
        HelpViewModel viewModel = new HelpViewModel(
                new RequestReviewFlowUseCase(repository),
                new LaunchReviewFlowUseCase(repository)
        );
        Activity activity = Mockito.mock(Activity.class);
        ReviewInfo info = Mockito.mock(ReviewInfo.class);

        viewModel.launchReviewFlow(activity, info);

        assertSame(activity, repository.lastActivity);
        assertSame(info, repository.lastReviewInfo);
    }

    private static final class FakeHelpRepository implements HelpRepository {
        private HelpRepository.OnReviewInfoListener lastListener;
        private Activity lastActivity;
        private ReviewInfo lastReviewInfo;

        @Override
        public void requestReviewFlow(OnReviewInfoListener listener) {
            lastListener = listener;
        }

        @Override
        public void launchReviewFlow(Activity activity, ReviewInfo reviewInfo) {
            lastActivity = activity;
            lastReviewInfo = reviewInfo;
        }
    }
}
