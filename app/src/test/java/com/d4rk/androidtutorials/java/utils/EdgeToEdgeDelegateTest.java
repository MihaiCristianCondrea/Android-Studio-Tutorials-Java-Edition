package com.d4rk.androidtutorials.java.utils;

import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class EdgeToEdgeDelegateTest {

    @Test
    public void apply_setsContainerPaddingToSystemBarInsets() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        View container = new View(activity);

        EdgeToEdgeDelegate.apply(activity, container);

        Insets bars = Insets.of(4, 8, 12, 16);
        WindowInsetsCompat windowInsets = new WindowInsetsCompat.Builder()
                .setInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(), bars)
                .build();

        ViewCompat.dispatchApplyWindowInsets(container, windowInsets);

        assertEquals(bars.left, container.getPaddingLeft());
        assertEquals(bars.top, container.getPaddingTop());
        assertEquals(bars.right, container.getPaddingRight());
        assertEquals(bars.bottom, container.getPaddingBottom());
    }

    @Test
    public void applyBottomBar_setsContainerTopPaddingAndBottomBarPadding() {
        Activity activity = Robolectric.buildActivity(Activity.class).setup().get();
        View container = new View(activity);
        View bottomBar = new View(activity);

        EdgeToEdgeDelegate.applyBottomBar(activity, container, bottomBar);

        Insets bars = Insets.of(3, 6, 9, 12);
        WindowInsetsCompat windowInsets = new WindowInsetsCompat.Builder()
                .setInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(), bars)
                .build();

        ViewCompat.dispatchApplyWindowInsets(container, windowInsets);

        assertEquals(bars.left, container.getPaddingLeft());
        assertEquals(bars.top, container.getPaddingTop());
        assertEquals(bars.right, container.getPaddingRight());
        assertEquals(0, container.getPaddingBottom());

        assertEquals(0, bottomBar.getPaddingLeft());
        assertEquals(0, bottomBar.getPaddingTop());
        assertEquals(0, bottomBar.getPaddingRight());
        assertEquals(bars.bottom, bottomBar.getPaddingBottom());
    }
}
