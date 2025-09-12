package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import dagger.hilt.android.qualifiers.ApplicationContext;

@HiltViewModel
public class OnboardingViewModel extends ViewModel {

    private final Context context;
    private final SharedPreferences prefs;

    @Inject
    public OnboardingViewModel(@ApplicationContext Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setTheme(String value) {
        prefs.edit().putString(context.getString(R.string.key_theme), value).apply();
    }

    public void setDefaultTab(String value) {
        prefs.edit().putString(context.getString(R.string.key_default_tab), value).apply();
    }

    public void setBottomNavLabels(String value) {
        prefs.edit().putString(context.getString(R.string.key_bottom_navigation_bar_labels), value).apply();
    }

    public void setMonospaceFont(String value) {
        prefs.edit().putString(context.getString(R.string.key_monospace_font), value).apply();
    }

    public void markOnboardingComplete() {
        prefs.edit().putBoolean(context.getString(R.string.key_onboarding_complete), true).apply();
    }
}

