package com.d4rk.androidtutorials.java.domain.settings;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

import org.junit.Test;

public class OnPreferenceChangedUseCaseTest {

    @Test
    public void invokeDelegatesAndReturnsThemeChange() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.applyTheme()).thenReturn(true);
        OnPreferenceChangedUseCase useCase = new OnPreferenceChangedUseCase(repository);

        boolean changed = useCase.invoke("dark_mode");

        assertTrue(changed);
        verify(repository).handlePreferenceChange("dark_mode");
        verify(repository).applyTheme();
    }

    @Test
    public void invokeReturnsFalseWhenThemeNotChanged() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.applyTheme()).thenReturn(false);
        OnPreferenceChangedUseCase useCase = new OnPreferenceChangedUseCase(repository);

        boolean changed = useCase.invoke("notifications");

        assertFalse(changed);
        verify(repository).handlePreferenceChange("notifications");
        verify(repository).applyTheme();
    }

    @Test
    public void invokeSupportsNullKey() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.applyTheme()).thenReturn(false);
        OnPreferenceChangedUseCase useCase = new OnPreferenceChangedUseCase(repository);

        useCase.invoke(null);

        verify(repository).handlePreferenceChange(null);
        verify(repository).applyTheme();
    }

    @Test
    public void invokePropagatesExceptionFromHandle() {
        SettingsRepository repository = mock(SettingsRepository.class);
        doThrow(new IllegalStateException("error"))
                .when(repository).handlePreferenceChange("key");
        OnPreferenceChangedUseCase useCase = new OnPreferenceChangedUseCase(repository);

        assertThrows(IllegalStateException.class, () -> useCase.invoke("key"));
        verify(repository).handlePreferenceChange("key");
    }

    @Test
    public void invokePropagatesExceptionFromApplyTheme() {
        SettingsRepository repository = mock(SettingsRepository.class);
        when(repository.applyTheme()).thenThrow(new IllegalStateException("apply"));
        OnPreferenceChangedUseCase useCase = new OnPreferenceChangedUseCase(repository);

        assertThrows(IllegalStateException.class, () -> useCase.invoke("key"));
        verify(repository).handlePreferenceChange("key");
        verify(repository).applyTheme();
    }
}
