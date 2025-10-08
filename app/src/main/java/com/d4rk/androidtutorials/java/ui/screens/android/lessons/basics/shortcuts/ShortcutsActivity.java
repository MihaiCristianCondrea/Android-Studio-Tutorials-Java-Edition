package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityShortcutsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs.ShortcutsCategoryActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class ShortcutsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShortcutsBinding binding = ActivityShortcutsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        AdUtils.loadBanner(binding.adViewBottom);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shortcuts, new SettingsFragment()).commit();
        binding.buttonMore.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/studio/intro/keyboard-shortcuts"))));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_shortcuts, rootKey);
            setupPreference("shortcuts_general", R.layout.activity_shortcuts_general, R.string.general);
            setupPreference("shortcuts_refactoring", R.layout.activity_shortcuts_refractoring, R.string.refactoring);
            setupPreference("shortcuts_build", R.layout.activity_shortcuts_build, R.string.build_and_run);
            setupPreference("shortcuts_debugging", R.layout.activity_shortcuts_debugging, R.string.debugging);
            setupPreference("shortcuts_navigation", R.layout.activity_shortcuts_navigation_and_searching, R.string.navigation_and_searching);
            setupPreference("shortcuts_code", R.layout.activity_shortcuts_code, R.string.writing_code);
            setupPreference("shortcuts_version_control", R.layout.activity_shortcuts_version_control, R.string.version_control);
        }

        private void setupPreference(String key, int layoutResId, int titleResId) {
            Preference preference = findPreference(key);
            if (preference == null) {
                return;
            }

            preference.setOnPreferenceClickListener(pref -> {
                Intent intent = new Intent(requireContext(), ShortcutsCategoryActivity.class);
                intent.putExtra(ShortcutsCategoryActivity.EXTRA_LAYOUT_RES_ID, layoutResId);
                intent.putExtra(ShortcutsCategoryActivity.EXTRA_TITLE_RES_ID, titleResId);
                startActivity(intent);
                return true;
            });
        }
    }
}
