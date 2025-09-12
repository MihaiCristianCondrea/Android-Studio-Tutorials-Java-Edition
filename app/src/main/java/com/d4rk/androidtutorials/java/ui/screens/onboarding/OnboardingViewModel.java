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
    private int currentPage = 0;

    @Inject
    public OnboardingViewModel(@ApplicationContext Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setTheme(String value) {
        prefs.edit().putString(context.getString(R.string.key_theme), value).apply();
    }

    public String getTheme() {
        String[] values = context.getResources().getStringArray(R.array.preference_theme_values);
        return prefs.getString(context.getString(R.string.key_theme), values[0]);
    }

    public void setCurrentPage(int page) {
        currentPage = page;
    }

    public int getCurrentPage() {
        return currentPage;
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

    public void setCrashlyticsEnabled(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.key_firebase_crashlytics), enabled).apply();
    }

    public void setConsentAnalytics(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.key_consent_analytics), enabled).apply();
    }

    public void setConsentAdStorage(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.key_consent_ad_storage), enabled).apply();
    }

    public void setConsentAdUserData(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.key_consent_ad_user_data), enabled).apply();
    }

    public void setConsentAdPersonalization(boolean enabled) {
        prefs.edit().putBoolean(context.getString(R.string.key_consent_ad_personalization), enabled).apply();
    }

    public void markOnboardingComplete() {
        prefs.edit().putBoolean(context.getString(R.string.key_onboarding_complete), true).apply();
    }
}

