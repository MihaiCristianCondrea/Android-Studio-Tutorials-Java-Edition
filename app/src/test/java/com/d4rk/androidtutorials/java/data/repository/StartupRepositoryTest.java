package com.d4rk.androidtutorials.java.data.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.content.Context;

import com.d4rk.androidtutorials.java.ui.screens.startup.repository.StartupRepository;
import com.google.android.ump.ConsentForm;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.FormError;
import com.google.android.ump.UserMessagingPlatform;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class StartupRepositoryTest {

    @Test
    public void requestConsentInfoUpdate_runsOnSuccessCallback() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters parameters = mock(ConsentRequestParameters.class);
        ConsentInformation consentInformation = mock(ConsentInformation.class);
        Runnable onSuccess = mock(Runnable.class);
        StartupRepository.OnFormError onFormError = mock(StartupRepository.OnFormError.class);

        try (MockedStatic<UserMessagingPlatform> umpMockedStatic = Mockito.mockStatic(UserMessagingPlatform.class)) {
            umpMockedStatic.when(() -> UserMessagingPlatform.getConsentInformation(context)).thenReturn(consentInformation);
            StartupRepository repository = new StartupRepository(context);

            doAnswer(invocation -> {
                ConsentInformation.OnConsentInfoUpdateSuccessListener successListener = invocation.getArgument(2);
                successListener.onConsentInfoUpdateSuccess();
                return null;
            }).when(consentInformation).requestConsentInfoUpdate(eq(activity), eq(parameters), any(), any());

            repository.requestConsentInfoUpdate(activity, parameters, onSuccess, onFormError);

            verify(consentInformation).requestConsentInfoUpdate(eq(activity), eq(parameters), any(), any());
            verify(onSuccess).run();
            verify(onFormError, never()).onFormError(any());
        }
    }

    @Test
    public void requestConsentInfoUpdate_failureInvokesOnError() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters parameters = mock(ConsentRequestParameters.class);
        ConsentInformation consentInformation = mock(ConsentInformation.class);
        StartupRepository.OnFormError onFormError = mock(StartupRepository.OnFormError.class);
        FormError formError = mock(FormError.class);

        try (MockedStatic<UserMessagingPlatform> umpMockedStatic = Mockito.mockStatic(UserMessagingPlatform.class)) {
            umpMockedStatic.when(() -> UserMessagingPlatform.getConsentInformation(context)).thenReturn(consentInformation);
            StartupRepository repository = new StartupRepository(context);

            doAnswer(invocation -> {
                ConsentInformation.OnConsentInfoUpdateFailureListener failureListener = invocation.getArgument(3);
                failureListener.onConsentInfoUpdateFailure(formError);
                return null;
            }).when(consentInformation).requestConsentInfoUpdate(eq(activity), eq(parameters), any(), any());

            repository.requestConsentInfoUpdate(activity, parameters, () -> {}, onFormError);

            verify(onFormError).onFormError(formError);
        }
    }

    @Test
    public void requestConsentInfoUpdate_failureWithNullErrorCallbackDoesNotCrash() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        ConsentRequestParameters parameters = mock(ConsentRequestParameters.class);
        ConsentInformation consentInformation = mock(ConsentInformation.class);
        FormError formError = mock(FormError.class);

        try (MockedStatic<UserMessagingPlatform> umpMockedStatic = Mockito.mockStatic(UserMessagingPlatform.class)) {
            umpMockedStatic.when(() -> UserMessagingPlatform.getConsentInformation(context)).thenReturn(consentInformation);
            StartupRepository repository = new StartupRepository(context);

            doAnswer(invocation -> {
                ConsentInformation.OnConsentInfoUpdateFailureListener failureListener = invocation.getArgument(3);
                failureListener.onConsentInfoUpdateFailure(formError);
                return null;
            }).when(consentInformation).requestConsentInfoUpdate(eq(activity), eq(parameters), any(), any());

            repository.requestConsentInfoUpdate(activity, parameters, () -> {}, null);

            verify(consentInformation).requestConsentInfoUpdate(eq(activity), eq(parameters), any(), any());
        }
    }

    @Test
    public void loadConsentForm_whenRequiredShowsForm() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        ConsentInformation consentInformation = mock(ConsentInformation.class);
        ConsentForm consentForm = mock(ConsentForm.class);

        try (MockedStatic<UserMessagingPlatform> umpMockedStatic = Mockito.mockStatic(UserMessagingPlatform.class)) {
            umpMockedStatic.when(() -> UserMessagingPlatform.getConsentInformation(context)).thenReturn(consentInformation);
            StartupRepository repository = new StartupRepository(context);
            when(consentInformation.getConsentStatus()).thenReturn(ConsentInformation.ConsentStatus.REQUIRED);

            umpMockedStatic.when(() -> UserMessagingPlatform.loadConsentForm(eq(activity), any(), any())).thenAnswer(invocation -> {
                UserMessagingPlatform.OnConsentFormLoadSuccessListener successListener = invocation.getArgument(1);
                successListener.onConsentFormLoadSuccess(consentForm);
                return null;
            });

            repository.loadConsentForm(activity, null);

            verify(consentForm).show(eq(activity), any());
        }
    }

    @Test
    public void loadConsentForm_failureInvokesOnError() {
        Context context = mock(Context.class);
        Activity activity = mock(Activity.class);
        ConsentInformation consentInformation = mock(ConsentInformation.class);
        StartupRepository.OnFormError onFormError = mock(StartupRepository.OnFormError.class);
        FormError formError = mock(FormError.class);

        try (MockedStatic<UserMessagingPlatform> umpMockedStatic = Mockito.mockStatic(UserMessagingPlatform.class)) {
            umpMockedStatic.when(() -> UserMessagingPlatform.getConsentInformation(context)).thenReturn(consentInformation);
            StartupRepository repository = new StartupRepository(context);

            umpMockedStatic.when(() -> UserMessagingPlatform.loadConsentForm(eq(activity), any(), any())).thenAnswer(invocation -> {
                UserMessagingPlatform.OnConsentFormLoadFailureListener failureListener = invocation.getArgument(2);
                failureListener.onConsentFormLoadFailure(formError);
                return null;
            });

            repository.loadConsentForm(activity, onFormError);

            verify(onFormError).onFormError(formError);
        }
    }
}
