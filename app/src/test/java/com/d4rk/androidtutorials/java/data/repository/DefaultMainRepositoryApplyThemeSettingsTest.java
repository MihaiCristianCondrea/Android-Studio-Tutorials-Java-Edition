package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 33)
public class DefaultMainRepositoryApplyThemeSettingsTest {

    private static final String[] DARK_MODE_VALUES = {
            "MODE_NIGHT_FOLLOW_SYSTEM",
            "MODE_NIGHT_NO",
            "MODE_NIGHT_YES",
            "MODE_NIGHT_AUTO_BATTERY"
    };

    private DefaultMainRepository createRepository(String prefValue) {
        Context context = RuntimeEnvironment.getApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().putString(context.getString(R.string.key_theme), prefValue).apply();
        return new DefaultMainRepository(context);
    }

    @Test
    public void applyThemeSettingsChangesModeWhenDifferent() {
        DefaultMainRepository repo = createRepository("MODE_NIGHT_YES");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        assertTrue(repo.applyThemeSettings(DARK_MODE_VALUES));
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, AppCompatDelegate.getDefaultNightMode());
    }

    @Test
    public void applyThemeSettingsNoChangeWhenSame() {
        DefaultMainRepository repo = createRepository("MODE_NIGHT_NO");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        assertFalse(repo.applyThemeSettings(DARK_MODE_VALUES));
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.getDefaultNightMode());
    }
}
