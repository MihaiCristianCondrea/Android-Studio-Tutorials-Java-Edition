package com.d4rk.androidtutorials.java.domain.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

import org.junit.Test;

public class GetDarkModeUseCaseTest {

    @Test
    public void invokeReturnsDarkModeValue() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.getDarkMode()).thenReturn("dark");
        GetDarkModeUseCase useCase = new GetDarkModeUseCase(repository);

        String result = useCase.invoke();

        assertEquals("dark", result);
        verify(repository).getDarkMode();
    }

    @Test
    public void invokeReturnsNullWhenRepositoryReturnsNull() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.getDarkMode()).thenReturn(null);
        GetDarkModeUseCase useCase = new GetDarkModeUseCase(repository);

        String result = useCase.invoke();

        assertNull(result);
        verify(repository).getDarkMode();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.getDarkMode()).thenThrow(new IllegalStateException("fail"));
        GetDarkModeUseCase useCase = new GetDarkModeUseCase(repository);

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).getDarkMode();
    }
}
