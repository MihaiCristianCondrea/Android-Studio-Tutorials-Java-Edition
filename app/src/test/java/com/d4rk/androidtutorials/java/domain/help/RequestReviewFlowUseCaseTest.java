package com.d4rk.androidtutorials.java.domain.help;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.HelpRepository;
import com.google.android.play.core.review.ReviewInfo;

import org.junit.Test;

public class RequestReviewFlowUseCaseTest {

    @Test
    public void invokeDelegatesToRepository() {
        HelpRepository repository = mock(HelpRepository.class);
        HelpRepository.OnReviewInfoListener listener = mock(HelpRepository.OnReviewInfoListener.class);
        RequestReviewFlowUseCase useCase = new RequestReviewFlowUseCase(repository);

        useCase.invoke(listener);

        verify(repository).requestReviewFlow(listener);
    }

    @Test
    public void invokeAcceptsNullListener() {
        HelpRepository repository = mock(HelpRepository.class);
        RequestReviewFlowUseCase useCase = new RequestReviewFlowUseCase(repository);

        useCase.invoke(null);

        verify(repository).requestReviewFlow(null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        HelpRepository repository = mock(HelpRepository.class);
        HelpRepository.OnReviewInfoListener listener = mock(HelpRepository.OnReviewInfoListener.class);
        RequestReviewFlowUseCase useCase = new RequestReviewFlowUseCase(repository);
        doThrow(new IllegalStateException("error"))
                .when(repository).requestReviewFlow(listener);

        assertThrows(IllegalStateException.class, () -> useCase.invoke(listener));
        verify(repository).requestReviewFlow(listener);
    }

    @Test
    public void invokePropagatesSuccessCallback() {
        HelpRepository repository = mock(HelpRepository.class);
        HelpRepository.OnReviewInfoListener listener = mock(HelpRepository.OnReviewInfoListener.class);
        RequestReviewFlowUseCase useCase = new RequestReviewFlowUseCase(repository);
        ReviewInfo info = mock(ReviewInfo.class);
        doAnswer(invocation -> {
            HelpRepository.OnReviewInfoListener callback = invocation.getArgument(0);
            callback.onSuccess(info);
            return null;
        }).when(repository).requestReviewFlow(listener);

        useCase.invoke(listener);

        verify(repository).requestReviewFlow(listener);
        verify(listener).onSuccess(info);
    }

    @Test
    public void invokePropagatesFailureCallback() {
        HelpRepository repository = mock(HelpRepository.class);
        HelpRepository.OnReviewInfoListener listener = mock(HelpRepository.OnReviewInfoListener.class);
        RequestReviewFlowUseCase useCase = new RequestReviewFlowUseCase(repository);
        Exception error = new Exception("fail");
        doAnswer(invocation -> {
            HelpRepository.OnReviewInfoListener callback = invocation.getArgument(0);
            callback.onFailure(error);
            return null;
        }).when(repository).requestReviewFlow(listener);

        useCase.invoke(listener);

        verify(repository).requestReviewFlow(listener);
        verify(listener).onFailure(error);
    }
}
