package com.d4rk.androidtutorials.java.ui.screens.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.ListPreference;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivitySettingsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends UpNavigationActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        androidx.preference.Preference.SummaryProvider<ListPreference> {

    private SettingsViewModel settingsViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        settingsViewModel.applyConsent();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();


        settingsViewModel.registerPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        boolean changedTheme = settingsViewModel.onPreferenceChanged(key);
        if (changedTheme) {
            recreate();
        }
    }

    /**
     * Provide summary for ListPreference if needed
     */
    @Override
    public CharSequence provideSummary(ListPreference preference) {
        String key = preference.getKey();
        if (key != null && key.equals(getString(R.string.dark_mode))) {
            String value = settingsViewModel.getDarkMode();
            int index = preference.findIndexOfValue(value);
            if (index >= 0) {
                CharSequence[] entries = preference.getEntries();
                if (entries != null && index < entries.length) {
                    return entries[index];
                }
            }
        }
        return null;
    }

    public SettingsViewModel getSettingsViewModel() {
        return settingsViewModel;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        settingsViewModel.unregisterPreferenceChangeListener(this);
    }
}