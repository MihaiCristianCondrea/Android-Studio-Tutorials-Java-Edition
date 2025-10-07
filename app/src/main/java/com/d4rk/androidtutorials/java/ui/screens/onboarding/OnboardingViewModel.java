package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
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

        // Ensure defaults are persisted so skipping onboarding still sets them
        String themeKey = context.getString(R.string.key_theme);
        String[] themeValues = context.getResources().getStringArray(R.array.preference_theme_values);
        if (!prefs.contains(themeKey)) {
            prefs.edit().putString(themeKey, themeValues[0]).apply();
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }

        String tabKey = context.getString(R.string.key_default_tab);
        String[] tabValues = context.getResources().getStringArray(R.array.preference_default_tab_values);
        if (!prefs.contains(tabKey)) {
            prefs.edit().putString(tabKey, tabValues[0]).apply();
        }
    }

    public String getTheme() {
        String[] values = context.getResources().getStringArray(R.array.preference_theme_values);
        return prefs.getString(context.getString(R.string.key_theme), values[0]);
    }

    public void setTheme(String value) {
        prefs.edit().putString(context.getString(R.string.key_theme), value).apply();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int page) {
        currentPage = page;
    }

    public String getDefaultTab() {
        String[] values = context.getResources().getStringArray(R.array.preference_default_tab_values);
        return prefs.getString(context.getString(R.string.key_default_tab), values[0]);
    }

    public void setDefaultTab(String value) {
        prefs.edit().putString(context.getString(R.string.key_default_tab), value).apply();
    }

    public String getBottomNavLabels() {
        String[] values = context.getResources().getStringArray(R.array.preference_bottom_navigation_bar_labels_values);
        return prefs.getString(context.getString(R.string.key_bottom_navigation_bar_labels), values[0]);
    }

    public void setBottomNavLabels(String value) {
        prefs.edit().putString(context.getString(R.string.key_bottom_navigation_bar_labels), value).apply();
    }

    public String getMonospaceFont() {
        String[] values = context.getResources().getStringArray(R.array.code_font_values);
        return prefs.getString(context.getString(R.string.key_monospace_font), values[0]);
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

