package com.d4rk.androidtutorials.java.domain.settings;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

import org.junit.Test;

public class ApplyConsentUseCaseTest {

    @Test
    public void invokeCallsRepository() {
        SettingsRepository repository = mock(SettingsRepository.class);
        ApplyConsentUseCase useCase = new ApplyConsentUseCase(repository);

        useCase.invoke();

        verify(repository).applyConsent();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SettingsRepository repository = mock(SettingsRepository.class);
        ApplyConsentUseCase useCase = new ApplyConsentUseCase(repository);
        doThrow(new IllegalStateException("error"))
                .when(repository).applyConsent();

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).applyConsent();
    }
}
