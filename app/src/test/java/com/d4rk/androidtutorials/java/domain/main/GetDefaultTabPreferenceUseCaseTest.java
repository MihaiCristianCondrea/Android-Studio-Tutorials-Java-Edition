package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

import org.junit.Test;

public class GetDefaultTabPreferenceUseCaseTest {

    @Test
    public void invokeReturnsPreference() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getDefaultTabPreference()).thenReturn("home");
        GetDefaultTabPreferenceUseCase useCase = new GetDefaultTabPreferenceUseCase(repository);

        String result = useCase.invoke();

        assertEquals("home", result);
        verify(repository).getDefaultTabPreference();
    }

    @Test
    public void invokeReturnsNullWhenRepositoryReturnsNull() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getDefaultTabPreference()).thenReturn(null);
        GetDefaultTabPreferenceUseCase useCase = new GetDefaultTabPreferenceUseCase(repository);

        String result = useCase.invoke();

        assertNull(result);
        verify(repository).getDefaultTabPreference();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.getDefaultTabPreference())
                .thenThrow(new IllegalStateException("fail"));
        GetDefaultTabPreferenceUseCase useCase = new GetDefaultTabPreferenceUseCase(repository);

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).getDefaultTabPreference();
    }
}
