package com.d4rk.androidtutorials.java.ui.screens.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;

import com.d4rk.androidtutorials.java.domain.settings.ApplyConsentUseCase;
import com.d4rk.androidtutorials.java.domain.settings.GetDarkModeUseCase;
import com.d4rk.androidtutorials.java.domain.settings.OnPreferenceChangedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.RegisterPreferenceChangeListenerUseCase;
import com.d4rk.androidtutorials.java.domain.settings.SetConsentAcceptedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.UnregisterPreferenceChangeListenerUseCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SettingsViewModelTest {

    @Mock
    private OnPreferenceChangedUseCase onPreferenceChangedUseCase;

    @Mock
    private RegisterPreferenceChangeListenerUseCase registerPreferenceChangeListenerUseCase;

    @Mock
    private UnregisterPreferenceChangeListenerUseCase unregisterPreferenceChangeListenerUseCase;

    @Mock
    private GetDarkModeUseCase getDarkModeUseCase;

    @Mock
    private SetConsentAcceptedUseCase setConsentAcceptedUseCase;

    @Mock
    private ApplyConsentUseCase applyConsentUseCase;

    private SettingsViewModel viewModel;

    @Before
    public void setUp() {
        viewModel = new SettingsViewModel(
                onPreferenceChangedUseCase,
                registerPreferenceChangeListenerUseCase,
                unregisterPreferenceChangeListenerUseCase,
                getDarkModeUseCase,
                setConsentAcceptedUseCase,
                applyConsentUseCase
        );
    }

    @Test
    public void onPreferenceChanged_nullKeyReturnsFalse() {
        assertFalse(viewModel.onPreferenceChanged(null));
        verifyNoInteractions(onPreferenceChangedUseCase);
    }

    @Test
    public void onPreferenceChanged_delegatesToUseCase() {
        when(onPreferenceChangedUseCase.invoke("key"))
                .thenReturn(true);

        assertTrue(viewModel.onPreferenceChanged("key"));
        verify(onPreferenceChangedUseCase).invoke("key");
    }

    @Test
    public void onPreferenceChanged_propagatesFailure() {
        when(onPreferenceChangedUseCase.invoke("error"))
                .thenThrow(new IllegalArgumentException("fail"));

        assertThrows(IllegalArgumentException.class, () -> viewModel.onPreferenceChanged("error"));
    }

    @Test
    public void applyConsent_invokesUseCase() {
        viewModel.applyConsent();

        verify(applyConsentUseCase).invoke();
    }

    @Test
    public void applyConsent_propagatesFailure() {
        doThrow(new IllegalStateException("boom")).when(applyConsentUseCase).invoke();

        assertThrows(IllegalStateException.class, () -> viewModel.applyConsent());
    }

    @Test
    public void registerPreferenceChangeListener_delegates() {
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                mock(SharedPreferences.OnSharedPreferenceChangeListener.class);

        viewModel.registerPreferenceChangeListener(listener);

        verify(registerPreferenceChangeListenerUseCase).invoke(listener);
    }

    @Test
    public void registerPreferenceChangeListener_propagatesFailure() {
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
        doThrow(new RuntimeException("error"))
                .when(registerPreferenceChangeListenerUseCase)
                .invoke(listener);

        assertThrows(RuntimeException.class,
                () -> viewModel.registerPreferenceChangeListener(listener));
    }

    @Test
    public void unregisterPreferenceChangeListener_delegates() {
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                mock(SharedPreferences.OnSharedPreferenceChangeListener.class);

        viewModel.unregisterPreferenceChangeListener(listener);

        verify(unregisterPreferenceChangeListenerUseCase).invoke(listener);
    }

    @Test
    public void unregisterPreferenceChangeListener_propagatesFailure() {
        SharedPreferences.OnSharedPreferenceChangeListener listener =
                mock(SharedPreferences.OnSharedPreferenceChangeListener.class);
        doThrow(new IllegalStateException("oops"))
                .when(unregisterPreferenceChangeListenerUseCase)
                .invoke(listener);

        assertThrows(IllegalStateException.class,
                () -> viewModel.unregisterPreferenceChangeListener(listener));
    }

    @Test
    public void getDarkMode_delegatesToUseCase() {
        when(getDarkModeUseCase.invoke()).thenReturn("dark");

        assertEquals("dark", viewModel.getDarkMode());
        verify(getDarkModeUseCase).invoke();
    }

    @Test
    public void getDarkMode_propagatesFailure() {
        when(getDarkModeUseCase.invoke()).thenThrow(new IllegalStateException("oops"));

        assertThrows(IllegalStateException.class, () -> viewModel.getDarkMode());
    }

    @Test
    public void setConsentAccepted_delegatesToUseCase() {
        viewModel.setConsentAccepted(true);
        viewModel.setConsentAccepted(false);

        verify(setConsentAcceptedUseCase).invoke(true);
        verify(setConsentAcceptedUseCase).invoke(false);
    }

    @Test
    public void setConsentAccepted_propagatesFailure() {
        doThrow(new IllegalArgumentException("fail"))
                .when(setConsentAcceptedUseCase).invoke(true);

        assertThrows(IllegalArgumentException.class, () -> viewModel.setConsentAccepted(true));
    }
}

