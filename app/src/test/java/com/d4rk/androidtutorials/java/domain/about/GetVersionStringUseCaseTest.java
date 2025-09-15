package com.d4rk.androidtutorials.java.domain.about;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.AboutRepository;

import org.junit.Test;

public class GetVersionStringUseCaseTest {

    @Test
    public void invokeReturnsRepositoryVersionString() {
        AboutRepository repository = mock(AboutRepository.class);
        when(repository.getVersionString()).thenReturn("v9.9.9");
        GetVersionStringUseCase useCase = new GetVersionStringUseCase(repository);

        String result = useCase.invoke();

        assertEquals("v9.9.9", result);
        verify(repository).getVersionString();
    }

    @Test
    public void invokeAllowsNullVersionString() {
        AboutRepository repository = mock(AboutRepository.class);
        when(repository.getVersionString()).thenReturn(null);
        GetVersionStringUseCase useCase = new GetVersionStringUseCase(repository);

        String result = useCase.invoke();

        assertNull(result);
        verify(repository).getVersionString();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        AboutRepository repository = mock(AboutRepository.class);
        when(repository.getVersionString()).thenThrow(new IllegalArgumentException("error"));
        GetVersionStringUseCase useCase = new GetVersionStringUseCase(repository);

        assertThrows(IllegalArgumentException.class, useCase::invoke);
        verify(repository).getVersionString();
    }
}
