package com.d4rk.androidtutorials.java.ui.screens.about.repository;

import android.content.Context;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Repository for the "About" screen. Provides version info, date strings, etc.
 */
public class AboutRepository implements com.d4rk.androidtutorials.java.data.repository.AboutRepository {

    private final Context context;

    public AboutRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Returns a formatted version string, like "v1.2.3 (Build 45)".
     */
    public String getVersionString() {
        String template = context.getString(R.string.app_version);
        String versionName = BuildConfig.VERSION_NAME;
        int versionCode = BuildConfig.VERSION_CODE;
        return String.format(template, versionName, versionCode);
    }

    /**
     * Returns the current year (as a string) in the user's default locale.
     */
    public String getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}