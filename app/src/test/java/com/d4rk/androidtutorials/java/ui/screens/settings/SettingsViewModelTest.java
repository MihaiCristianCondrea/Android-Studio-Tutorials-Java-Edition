package com.d4rk.androidtutorials.java.ui.screens.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.content.SharedPreferences;

import com.d4rk.androidtutorials.java.data.repository.SettingsRepository;
import com.d4rk.androidtutorials.java.domain.settings.ApplyConsentUseCase;
import com.d4rk.androidtutorials.java.domain.settings.GetDarkModeUseCase;
import com.d4rk.androidtutorials.java.domain.settings.OnPreferenceChangedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.RegisterPreferenceChangeListenerUseCase;
import com.d4rk.androidtutorials.java.domain.settings.SetConsentAcceptedUseCase;
import com.d4rk.androidtutorials.java.domain.settings.UnregisterPreferenceChangeListenerUseCase;

import org.junit.Test;

public class SettingsViewModelTest {

    @Test
    public void onPreferenceChangedDelegatesWhenKeyProvided() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        repository.themeResult = true;
        SettingsViewModel viewModel = createViewModel(repository);

        boolean result = viewModel.onPreferenceChanged("theme");

        assertTrue(result);
        assertEquals("theme", repository.lastChangedKey);
        assertTrue(repository.applyThemeCalled);
    }

    @Test
    public void onPreferenceChangedReturnsFalseWhenKeyNull() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        SettingsViewModel viewModel = createViewModel(repository);

        boolean result = viewModel.onPreferenceChanged(null);

        assertFalse(result);
        assertEquals(0, repository.changeCount);
    }

    @Test
    public void applyConsentDelegatesToRepository() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        SettingsViewModel viewModel = createViewModel(repository);

        viewModel.applyConsent();

        assertTrue(repository.applyConsentCalled);
    }

    @Test
    public void registerPreferenceChangeListenerDelegates() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        SettingsViewModel viewModel = createViewModel(repository);
        SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> { };

        viewModel.registerPreferenceChangeListener(listener);

        assertSame(listener, repository.registeredListener);
    }

    @Test
    public void unregisterPreferenceChangeListenerDelegates() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        SettingsViewModel viewModel = createViewModel(repository);
        SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> { };

        viewModel.unregisterPreferenceChangeListener(listener);

        assertSame(listener, repository.unregisteredListener);
    }

    @Test
    public void getDarkModeReturnsRepositoryValue() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        repository.darkMode = "dark";
        SettingsViewModel viewModel = createViewModel(repository);

        assertEquals("dark", viewModel.getDarkMode());
    }

    @Test
    public void setConsentAcceptedDelegates() {
        FakeSettingsRepository repository = new FakeSettingsRepository();
        SettingsViewModel viewModel = createViewModel(repository);

        viewModel.setConsentAccepted(true);

        assertTrue(repository.consentAccepted != null && repository.consentAccepted);
    }

    private SettingsViewModel createViewModel(FakeSettingsRepository repository) {
        return new SettingsViewModel(
                new OnPreferenceChangedUseCase(repository),
                new RegisterPreferenceChangeListenerUseCase(repository),
                new UnregisterPreferenceChangeListenerUseCase(repository),
                new GetDarkModeUseCase(repository),
                new SetConsentAcceptedUseCase(repository),
                new ApplyConsentUseCase(repository)
        );
    }

    private static final class FakeSettingsRepository implements SettingsRepository {
        private String lastChangedKey;
        private int changeCount;
        private boolean applyThemeCalled;
        private boolean themeResult;
        private boolean applyConsentCalled;
        private SharedPreferences.OnSharedPreferenceChangeListener registeredListener;
        private SharedPreferences.OnSharedPreferenceChangeListener unregisteredListener;
        private String darkMode = "system";
        private Boolean consentAccepted;

        @Override
        public void handlePreferenceChange(String key) {
            changeCount++;
            lastChangedKey = key;
        }

        @Override
        public boolean applyTheme() {
            applyThemeCalled = true;
            return themeResult;
        }

        @Override
        public void applyConsent() {
            applyConsentCalled = true;
        }

        @Override
        public void registerPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
            registeredListener = listener;
        }

        @Override
        public void unregisterPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
            unregisteredListener = listener;
        }

        @Override
        public String getDarkMode() {
            return darkMode;
        }

        @Override
        public void setConsentAccepted(boolean accepted) {
            consentAccepted = accepted;
        }
    }
}
