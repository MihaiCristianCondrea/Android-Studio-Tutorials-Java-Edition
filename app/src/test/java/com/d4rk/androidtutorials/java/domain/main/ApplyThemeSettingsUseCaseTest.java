package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

import org.junit.Test;

public class ApplyThemeSettingsUseCaseTest {

    @Test
    public void invokeReturnsRepositoryValue() {
        MainRepository repository = mock(MainRepository.class);
        String[] values = new String[]{"light", "dark"};
        when(repository.applyThemeSettings(values)).thenReturn(true);
        ApplyThemeSettingsUseCase useCase = new ApplyThemeSettingsUseCase(repository);

        boolean changed = useCase.invoke(values);

        assertTrue(changed);
        verify(repository).applyThemeSettings(values);
    }

    @Test
    public void invokeAllowsNullValues() {
        MainRepository repository = mock(MainRepository.class);
        when(repository.applyThemeSettings(null)).thenReturn(false);
        ApplyThemeSettingsUseCase useCase = new ApplyThemeSettingsUseCase(repository);

        boolean changed = useCase.invoke(null);

        assertFalse(changed);
        verify(repository).applyThemeSettings(null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        String[] values = new String[]{"system"};
        when(repository.applyThemeSettings(values))
                .thenThrow(new IllegalStateException("error"));
        ApplyThemeSettingsUseCase useCase = new ApplyThemeSettingsUseCase(repository);

        assertThrows(IllegalStateException.class,
                () -> useCase.invoke(values));
        verify(repository).applyThemeSettings(values);
    }
}
