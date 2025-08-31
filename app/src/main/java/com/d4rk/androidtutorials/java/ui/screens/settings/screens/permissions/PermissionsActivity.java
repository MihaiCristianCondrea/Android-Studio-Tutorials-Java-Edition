package com.d4rk.androidtutorials.java.ui.screens.settings.screens.permissions;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityPermissionsBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

public class PermissionsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPermissionsBinding binding = ActivityPermissionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_permissions, new SettingsFragment()).commit();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_permissions, rootKey);
        }
    }
}