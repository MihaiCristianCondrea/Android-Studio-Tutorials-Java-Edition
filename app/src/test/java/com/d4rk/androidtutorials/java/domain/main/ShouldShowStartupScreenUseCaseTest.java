package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

import org.junit.Test;

public class ShouldShowStartupScreenUseCaseTest {

    @Test
    public void invokeReturnsRepositoryValue() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.shouldShowStartupScreen()).thenReturn(true);
        ShouldShowStartupScreenUseCase useCase = new ShouldShowStartupScreenUseCase(repository);

        boolean result = useCase.invoke();

        assertTrue(result);
        verify(repository).shouldShowStartupScreen();
    }

    @Test
    public void invokeHandlesFalseValue() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.shouldShowStartupScreen()).thenReturn(false);
        ShouldShowStartupScreenUseCase useCase = new ShouldShowStartupScreenUseCase(repository);

        boolean result = useCase.invoke();

        assertFalse(result);
        verify(repository).shouldShowStartupScreen();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.shouldShowStartupScreen())
                .thenThrow(new IllegalStateException("error"));
        ShouldShowStartupScreenUseCase useCase = new ShouldShowStartupScreenUseCase(repository);

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).shouldShowStartupScreen();
    }
}
