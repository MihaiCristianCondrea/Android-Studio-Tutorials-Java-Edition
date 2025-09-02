package com.d4rk.androidtutorials.java.data.repository;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 33)
public class DefaultMainRepositoryBuildShortcutIntentTest {

    private DefaultMainRepository createRepository() {
        Context context = RuntimeEnvironment.getApplication();
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
