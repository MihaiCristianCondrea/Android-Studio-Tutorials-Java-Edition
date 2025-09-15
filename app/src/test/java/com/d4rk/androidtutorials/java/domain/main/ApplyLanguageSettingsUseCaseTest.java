package com.d4rk.androidtutorials.java.domain.main;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.d4rk.androidtutorials.java.data.repository.MainRepository;

import org.junit.Test;

public class ApplyLanguageSettingsUseCaseTest {

    @Test
    public void invokeCallsRepository() {
        MainRepository repository = mock(MainRepository.class);
        ApplyLanguageSettingsUseCase useCase = new ApplyLanguageSettingsUseCase(repository);

        useCase.invoke();

        verify(repository).applyLanguageSettings();
    }

    @Test
    public void invokePropagatesRepositoryException() {
        MainRepository repository = mock(MainRepository.class);
        ApplyLanguageSettingsUseCase useCase = new ApplyLanguageSettingsUseCase(repository);
        doThrow(new IllegalStateException("fail"))
                .when(repository).applyLanguageSettings();

        assertThrows(IllegalStateException.class, useCase::invoke);
        verify(repository).applyLanguageSettings();
    }
}
