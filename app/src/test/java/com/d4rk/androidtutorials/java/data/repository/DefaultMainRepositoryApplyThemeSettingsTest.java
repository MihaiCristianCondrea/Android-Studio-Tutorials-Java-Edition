package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import androidx.test.core.app.ApplicationProvider;

import com.d4rk.androidtutorials.java.R;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultMainRepositoryApplyThemeSettingsTest {

    private final String[] darkModeValues = {
            "MODE_NIGHT_FOLLOW_SYSTEM",
            "MODE_NIGHT_NO",
            "MODE_NIGHT_YES",
            "MODE_NIGHT_AUTO_BATTERY"
    };

    @After
    public void reset() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    @Test
    public void applyThemeSettings_updatesModeWhenDifferent() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        prefs.edit().putString(context.getString(R.string.key_theme), "MODE_NIGHT_YES").commit();

        DefaultMainRepository repo = new DefaultMainRepository(context);
        boolean changed = repo.applyThemeSettings(darkModeValues);

        assertTrue(changed);
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, AppCompatDelegate.getDefaultNightMode());
    }

    @Test
    public void applyThemeSettings_noChangeWhenSame() {
        Context context = ApplicationProvider.getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        prefs.edit().putString(context.getString(R.string.key_theme), "MODE_NIGHT_NO").commit();

        DefaultMainRepository repo = new DefaultMainRepository(context);
        boolean changed = repo.applyThemeSettings(darkModeValues);

        assertFalse(changed);
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.getDefaultNightMode());
    }
}
