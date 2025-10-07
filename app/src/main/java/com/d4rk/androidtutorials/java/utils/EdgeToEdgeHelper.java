package com.d4rk.androidtutorials.java.utils;

import android.view.View;
import android.view.Window;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;


public class EdgeToEdgeHelper {

    public static void applyEdgeToEdge(Window window, View view) {
        WindowCompat.enableEdgeToEdge(window);
        ViewCompat.setOnApplyWindowInsetsListener(view, (mView, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
            mView.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return WindowInsetsCompat.CONSUMED;
        });
    }
}
