package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

import org.junit.Test;

public class MarkStartupScreenShownUseCaseTest {

    @Test
    public void invokeCallsRepository() {
        MainRepository repository = mock(MainRepository.class);
        MarkStartupScreenShownUseCase useCase = new MarkStartupScreenShownUseCase(repository);

        useCase.invoke();

        verify(repository).markStartupScreenShown();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        MarkStartupScreenShownUseCase useCase = new MarkStartupScreenShownUseCase(repository);
        doThrow(new IllegalStateException("fail"))
                .when(repository).markStartupScreenShown();

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).markStartupScreenShown();
    }
}
