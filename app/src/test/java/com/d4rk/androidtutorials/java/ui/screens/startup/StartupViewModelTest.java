package com.d4rk.androidtutorials.java.ui.screens.startup;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.app.Activity;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.data.repository.StartupRepository.OnFormError;
import com.d4rk.androidtutorials.java.domain.startup.LoadConsentFormUseCase;
import com.d4rk.androidtutorials.java.domain.startup.RequestConsentInfoUseCase;
import com.google.android.ump.ConsentRequestParameters;

import org.junit.Rule;
import org.junit.Test;

public class StartupViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void requestConsentInfoUpdateDelegatesToUseCase() {
        RequestConsentInfoUseCase requestUseCase = mock(RequestConsentInfoUseCase.class);
        LoadConsentFormUseCase loadUseCase = mock(LoadConsentFormUseCase.class);
        StartupViewModel viewModel = new StartupViewModel(requestUseCase, loadUseCase);

        Activity activity = mock(Activity.class);
        ConsentRequestParameters params = mock(ConsentRequestParameters.class);
        Runnable onSuccess = mock(Runnable.class);
        OnFormError onError = mock(OnFormError.class);

        viewModel.requestConsentInfoUpdate(activity, params, onSuccess, onError);

        verify(requestUseCase).invoke(activity, params, onSuccess, onError);
    }

    @Test
    public void loadConsentFormDelegatesToUseCase() {
        RequestConsentInfoUseCase requestUseCase = mock(RequestConsentInfoUseCase.class);
        LoadConsentFormUseCase loadUseCase = mock(LoadConsentFormUseCase.class);
        StartupViewModel viewModel = new StartupViewModel(requestUseCase, loadUseCase);

        Activity activity = mock(Activity.class);
        OnFormError onError = mock(OnFormError.class);

        viewModel.loadConsentForm(activity, onError);

        verify(loadUseCase).invoke(activity, onError);
    }
}

