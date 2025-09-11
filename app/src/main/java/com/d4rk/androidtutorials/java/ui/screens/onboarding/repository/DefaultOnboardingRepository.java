package com.d4rk.androidtutorials.java.ui.screens.onboarding.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;

public class DefaultOnboardingRepository implements com.d4rk.androidtutorials.java.data.repository.OnboardingRepository {

    private final Context context;
    private final SharedPreferences prefs;

    public DefaultOnboardingRepository(Context context) {
        this.context = context.getApplicationContext();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this.context);
    }

    @Override
    public boolean isOnboardingComplete() {
        return prefs.getBoolean(context.getString(R.string.key_onboarding_complete), false);
    }

    @Override
    public void setOnboardingComplete() {
        prefs.edit().putBoolean(context.getString(R.string.key_onboarding_complete), true).apply();
        context.getSharedPreferences("startup", Context.MODE_PRIVATE)
                .edit().putBoolean("value", false).apply();
    }

    @Override
    public void setTheme(String value) {
        prefs.edit().putString(context.getString(R.string.key_theme), value).apply();
        String[] values = context.getResources().getStringArray(R.array.preference_theme_values);
        int mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        if (value.equals(values[1])) {
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (value.equals(values[2])) {
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        } else if (value.equals(values[3])) {
            mode = AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    @Override
    public void setDefaultTab(String value) {
        prefs.edit().putString(context.getString(R.string.key_default_tab), value).apply();
    }

    @Override
    public void setBottomBarLabels(String value) {
        prefs.edit().putString(context.getString(R.string.key_bottom_navigation_bar_labels), value).apply();
    }
}
