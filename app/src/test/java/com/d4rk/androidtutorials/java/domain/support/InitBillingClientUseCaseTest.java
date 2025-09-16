package com.d4rk.androidtutorials.java.domain.support;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.SupportRepository;

import org.junit.Test;

public class InitBillingClientUseCaseTest {

    @Test
    public void invokeDelegatesToRepository() {
        SupportRepository repository = mock(SupportRepository.class);
        Runnable onConnected = mock(Runnable.class);
        InitBillingClientUseCase useCase = new InitBillingClientUseCase(repository);

        useCase.invoke(onConnected);

        verify(repository).initBillingClient(onConnected);
    }

    @Test
    public void invokePropagatesConnectionCallback() {
        SupportRepository repository = mock(SupportRepository.class);
        Runnable onConnected = mock(Runnable.class);
        InitBillingClientUseCase useCase = new InitBillingClientUseCase(repository);
        doAnswer(invocation -> {
            Runnable callback = invocation.getArgument(0);
            callback.run();
            return null;
        }).when(repository).initBillingClient(onConnected);

        useCase.invoke(onConnected);

        verify(repository).initBillingClient(onConnected);
        verify(onConnected).run();
    }

    @Test
    public void invokeAllowsNullCallback() {
        SupportRepository repository = mock(SupportRepository.class);
        InitBillingClientUseCase useCase = new InitBillingClientUseCase(repository);

        useCase.invoke(null);

        verify(repository).initBillingClient(null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SupportRepository repository = mock(SupportRepository.class);
        Runnable onConnected = mock(Runnable.class);
        InitBillingClientUseCase useCase = new InitBillingClientUseCase(repository);
        doThrow(new IllegalStateException("failure"))
                .when(repository).initBillingClient(onConnected);

        assertThrows(IllegalStateException.class, () -> useCase.invoke(onConnected));
        verify(repository).initBillingClient(onConnected);
    }
}
