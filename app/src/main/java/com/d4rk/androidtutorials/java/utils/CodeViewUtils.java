package com.d4rk.androidtutorials.java.utils;

import android.graphics.Typeface;

import com.amrdeveloper.codeview.CodeView;

/**
 * Utility methods for configuring {@link CodeView} instances.
 */
public final class CodeViewUtils {

    /**
     * Apply common read–only settings to the provided CodeViews.
     *
     * @param typeface the typeface to set on each CodeView
     * @param views    one or more CodeView instances to configure
     */
    public static void applyDefaults(Typeface typeface, CodeView... views) {
        for (CodeView view : views) {
            if (view == null) continue;
            view.setTypeface(typeface);
            view.setTextSize(14f);
            view.setLineNumberTextSize(14f);
            view.setEnableLineNumber(false);
            view.setHorizontallyScrolling(false);
            view.setKeyListener(null);
            view.setCursorVisible(false);
            view.setTextIsSelectable(true);
            view.setHorizontalScrollBarEnabled(false);
            view.setVerticalScrollBarEnabled(false);
            view.setBackground(null);
        }
    }
}
