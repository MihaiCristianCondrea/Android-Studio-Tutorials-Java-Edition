package com.d4rk.androidtutorials.java.domain.settings;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;

import org.junit.Test;

public class SetConsentAcceptedUseCaseTest {

    @Test
    public void invokeDelegatesTrueValue() {
        SettingsRepository repository = mock(SettingsRepository.class);
        SetConsentAcceptedUseCase useCase = new SetConsentAcceptedUseCase(repository);

        useCase.invoke(true);

        verify(repository).setConsentAccepted(true);
    }

    @Test
    public void invokeDelegatesFalseValue() {
        SettingsRepository repository = mock(SettingsRepository.class);
        SetConsentAcceptedUseCase useCase = new SetConsentAcceptedUseCase(repository);

        useCase.invoke(false);

        verify(repository).setConsentAccepted(false);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        SettingsRepository repository = mock(SettingsRepository.class);
        SetConsentAcceptedUseCase useCase = new SetConsentAcceptedUseCase(repository);
        doThrow(new IllegalStateException("fail"))
                .when(repository).setConsentAccepted(true);

        assertThrows(IllegalStateException.class, () -> useCase.invoke(true));
        verify(repository).setConsentAccepted(true);
    }
}
