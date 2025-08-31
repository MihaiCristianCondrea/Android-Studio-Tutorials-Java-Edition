package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import androidx.preference.PreferenceFragmentCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityShortcutsBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class ShortcutsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShortcutsBinding binding = ActivityShortcutsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        MobileAds.initialize(this);
        binding.adViewBottom.loadAd(new AdRequest.Builder().build());

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_shortcuts, new SettingsFragment()).commit();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null)
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        binding.buttonMore.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/studio/intro/keyboard-shortcuts"))));
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_shortcuts, rootKey);
        }
    }
}