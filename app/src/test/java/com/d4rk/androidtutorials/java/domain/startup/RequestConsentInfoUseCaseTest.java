package com.d4rk.androidtutorials.java.domain.startup;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Activity;

import com.d4rk.androidtutorials.java.data.repository.StartupRepository;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;

import org.junit.Test;

public class RequestConsentInfoUseCaseTest {

    @Test
    public void invokeDelegatesToRepository() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters params = mock(ConsentRequestParameters.class);
        Runnable onSuccess = mock(Runnable.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        RequestConsentInfoUseCase useCase = new RequestConsentInfoUseCase(repository);

        useCase.invoke(activity, params, onSuccess, onError);

        verify(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);
    }

    @Test
    public void invokeSupportsNullParameters() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        Runnable onSuccess = mock(Runnable.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        RequestConsentInfoUseCase useCase = new RequestConsentInfoUseCase(repository);

        useCase.invoke(activity, null, onSuccess, onError);

        verify(repository).requestConsentInfoUpdate(activity, null, onSuccess, onError);
    }

    @Test
    public void invokeSupportsNullCallbacks() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters params = mock(ConsentRequestParameters.class);
        RequestConsentInfoUseCase useCase = new RequestConsentInfoUseCase(repository);

        useCase.invoke(activity, params, null, null);

        verify(repository).requestConsentInfoUpdate(activity, params, null, null);
    }

    @Test
    public void invokePropagatesSuccessCallback() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters params = mock(ConsentRequestParameters.class);
        Runnable onSuccess = mock(Runnable.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        RequestConsentInfoUseCase useCase = new RequestConsentInfoUseCase(repository);
        doAnswer(invocation -> {
            Runnable callback = invocation.getArgument(2);
            callback.run();
            return null;
        }).when(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);

        useCase.invoke(activity, params, onSuccess, onError);

        verify(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);
        verify(onSuccess).run();
    }

    @Test
    public void invokePropagatesFailureCallback() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters params = mock(ConsentRequestParameters.class);
        Runnable onSuccess = mock(Runnable.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        RequestConsentInfoUseCase useCase = new RequestConsentInfoUseCase(repository);
        FormError error = mock(FormError.class);
        doAnswer(invocation -> {
            StartupRepository.OnFormError callback = invocation.getArgument(3);
            callback.onFormError(error);
            return null;
        }).when(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);

        useCase.invoke(activity, params, onSuccess, onError);

        verify(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);
        verify(onError).onFormError(error);
    }

    @Test
    public void invokePropagatesRepositoryException() {
        StartupRepository repository = mock(StartupRepository.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters params = mock(ConsentRequestParameters.class);
        Runnable onSuccess = mock(Runnable.class);
        StartupRepository.OnFormError onError = mock(StartupRepository.OnFormError.class);
        RequestConsentInfoUseCase useCase = new RequestConsentInfoUseCase(repository);
        doThrow(new IllegalStateException("failure"))
                .when(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);

        assertThrows(IllegalStateException.class,
                () -> useCase.invoke(activity, params, onSuccess, onError));
        verify(repository).requestConsentInfoUpdate(activity, params, onSuccess, onError);
    }
}
