package com.d4rk.androidtutorials.java.domain.settings;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.SharedPreferences;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

import org.junit.Test;

public class UnregisterPreferenceChangeListenerUseCaseTest {

    @Test
    public void invokeUnregistersListener() {
        SettingsRepository repository = mock(SettingsRepository.class);
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
        UnregisterPreferenceChangeListenerUseCase useCase =
                new UnregisterPreferenceChangeListenerUseCase(repository);

        useCase.invoke(listener);

        verify(repository).unregisterPreferenceChangeListener(listener);
    }

    @Test
    public void invokeAllowsNullListener() {
        SettingsRepository repository = mock(SettingsRepository.class);
        UnregisterPreferenceChangeListenerUseCase useCase =
                new UnregisterPreferenceChangeListenerUseCase(repository);

        useCase.invoke(null);

        verify(repository).unregisterPreferenceChangeListener(null);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SettingsRepository repository = mock(SettingsRepository.class);
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
        UnregisterPreferenceChangeListenerUseCase useCase =
                new UnregisterPreferenceChangeListenerUseCase(repository);
        doThrow(new IllegalStateException("error"))
                .when(repository).unregisterPreferenceChangeListener(listener);

        assertThrows(IllegalStateException.class, () -> useCase.invoke(listener));
        verify(repository).unregisterPreferenceChangeListener(listener);
    }
}
