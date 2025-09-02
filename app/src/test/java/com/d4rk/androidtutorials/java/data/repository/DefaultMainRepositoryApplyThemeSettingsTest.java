package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
        Context context = mock(Context.class);
        SharedPreferences prefs = mock(SharedPreferences.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(prefs);
        when(context.getPackageName()).thenReturn("com.d4rk.androidtutorials");
        when(context.getString(R.string.key_theme)).thenReturn("theme");
        when(context.getString(R.string.default_value_theme)).thenReturn("MODE_NIGHT_FOLLOW_SYSTEM");
        when(prefs.getString(eq("theme"), anyString())).thenReturn(prefValue);
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
