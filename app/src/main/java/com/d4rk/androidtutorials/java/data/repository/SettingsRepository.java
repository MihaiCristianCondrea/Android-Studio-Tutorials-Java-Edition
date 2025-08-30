package com.d4rk.androidtutorials.java.data.repository;

import android.content.SharedPreferences;

public interface SettingsRepository {
    void handlePreferenceChange(String key);
    boolean applyTheme();
    void applyConsent();
    SharedPreferences getSharedPreferences();
}
