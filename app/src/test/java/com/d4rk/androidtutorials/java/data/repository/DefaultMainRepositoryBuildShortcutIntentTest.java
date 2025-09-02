package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class DefaultMainRepositoryBuildShortcutIntentTest {

    private DefaultMainRepository createRepository() {
        Context context = mock(Context.class);
        SharedPreferences prefs = mock(SharedPreferences.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getSharedPreferences(anyString(), anyInt())).thenReturn(prefs);
        when(context.getPackageName()).thenReturn("com.d4rk.androidtutorials");
        return new DefaultMainRepository(context);
    }

    @Test
    public void buildShortcutIntentInstalled() {
        DefaultMainRepository repo = createRepository();
        Intent intent = repo.buildShortcutIntent(true);
        assertEquals(Intent.ACTION_MAIN, intent.getAction());
        assertTrue(intent.getCategories().contains(Intent.CATEGORY_LAUNCHER));
        assertEquals("com.d4rk.androidtutorials", intent.getComponent().getPackageName());
        assertEquals("com.d4rk.androidtutorials.MainActivity", intent.getComponent().getClassName());
    }

    @Test
    public void buildShortcutIntentNotInstalled() {
        DefaultMainRepository repo = createRepository();
        Intent intent = repo.buildShortcutIntent(false);
        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals(Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.androidtutorials"), intent.getData());
    }
}
