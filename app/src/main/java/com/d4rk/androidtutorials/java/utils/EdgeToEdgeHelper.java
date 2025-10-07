package com.d4rk.androidtutorials.java.utils;

import android.content.res.Configuration;
import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class EdgeToEdgeHelper {

    public static void applyEdgeToEdge(Window window, View view) {
        WindowCompat.enableEdgeToEdge(window);
        applySystemBarsColor(window);
        ViewCompat.setOnApplyWindowInsetsListener(view, (mView, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            mView.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }

    public static void applySystemBarsColor(Window window) {
        boolean isLightMode = (window.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_NO;
        WindowInsetsControllerCompat controller = WindowCompat.getInsetsController(window, window.getDecorView());
        controller.setAppearanceLightNavigationBars(isLightMode);
        controller.setAppearanceLightStatusBars(isLightMode);
    }
}
