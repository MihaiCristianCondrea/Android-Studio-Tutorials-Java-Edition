package com.d4rk.androidtutorials.java.domain.startup;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.StartupRepository;
import com.google.android.ump.FormError;

import org.junit.Test;

public class LoadConsentFormUseCaseTest {

    @Test
    public void invokeDelegatesToRepository() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        LoadConsentFormUseCase useCase = new LoadConsentFormUseCase(repository);

        useCase.invoke(activity, onError);

        verify(repository).loadConsentForm(activity, onError);
    }

    @Test
    public void invokeAllowsNullActivity() {
        StartupRepository repository = mock(StartupRepository.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        LoadConsentFormUseCase useCase = new LoadConsentFormUseCase(repository);

        useCase.invoke(null, onError);

        verify(repository).loadConsentForm(null, onError);
    }

    @Test
    public void invokeAllowsNullErrorHandler() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        LoadConsentFormUseCase useCase = new LoadConsentFormUseCase(repository);

        useCase.invoke(activity, null);

        verify(repository).loadConsentForm(activity, null);
    }

    @Test
    public void invokePropagatesFormErrorCallback() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        LoadConsentFormUseCase useCase = new LoadConsentFormUseCase(repository);
        FormError formError = mock(FormError.class);
        doAnswer(invocation -> {
            StartupRepository.OnFormError callback = invocation.getArgument(1);
            callback.onFormError(formError);
            return null;
        }).when(repository).loadConsentForm(activity, onError);

        useCase.invoke(activity, onError);

        verify(repository).loadConsentForm(activity, onError);
        verify(onError).onFormError(formError);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        LoadConsentFormUseCase useCase = new LoadConsentFormUseCase(repository);
        doThrow(new IllegalStateException("error"))
                .when(repository).loadConsentForm(activity, onError);

        assertThrows(IllegalStateException.class, () -> useCase.invoke(activity, onError));
        verify(repository).loadConsentForm(activity, onError);
    }
}
