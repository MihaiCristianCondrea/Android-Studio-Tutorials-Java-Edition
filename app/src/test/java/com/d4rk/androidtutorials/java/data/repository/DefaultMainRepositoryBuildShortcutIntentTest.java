package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;

import static org.junit.Assert.*;

public class DefaultMainRepositoryBuildShortcutIntentTest {

    @Test
    public void buildShortcutIntent_installed() {
        Context context = ApplicationProvider.getApplicationContext();
        DefaultMainRepository repo = new DefaultMainRepository(context);

        Intent intent = repo.buildShortcutIntent(true);
        assertEquals(Intent.ACTION_MAIN, intent.getAction());
        assertTrue(intent.getCategories().contains(Intent.CATEGORY_LAUNCHER));
        assertEquals("com.d4rk.androidtutorials.MainActivity", intent.getComponent().getClassName());
    }

    @Test
    public void buildShortcutIntent_notInstalled() {
        Context context = ApplicationProvider.getApplicationContext();
        DefaultMainRepository repo = new DefaultMainRepository(context);

        Intent intent = repo.buildShortcutIntent(false);
        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals(Uri.parse("https://play.google.com/store/apps/details?id=com.d4rk.androidtutorials"), intent.getData());
    }
}
