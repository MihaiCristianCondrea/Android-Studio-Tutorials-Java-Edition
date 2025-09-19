package com.d4rk.androidtutorials.java.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d4rk.androidtutorials.java.R;

public final class EdgeToEdgeDelegate {

    private EdgeToEdgeDelegate() {
        // Utility class
    }

    public static void apply(Activity activity, View view) {
        enableEdgeToEdge(activity);
        applyInsetsAsPadding(
                view,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                true,
                true,
                true,
                true
        );
    }

    public static void applyBottomBar(Activity activity, View container, View bottomNavigationView) {
        enableEdgeToEdge(activity);
        applyInsetsAsPadding(
                container,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                true,
                true,
                true,
                false
        );
        applyInsetsAsPadding(
                bottomNavigationView,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                true,
                false,
                true,
                true
        );
    }

    private static void enableEdgeToEdge(Activity activity) {
        if (activity == null) {
            return;
        }
        WindowCompat.enableEdgeToEdge(activity.getWindow());
    }

    private static void applyInsetsAsPadding(View view, int insetTypes,
                                             boolean applyStart, boolean applyTop,
                                             boolean applyEnd, boolean applyBottom) {
        if (view == null) {
            return;
        }

        if (view instanceof ViewGroup viewGroup) {
            ViewGroupCompat.installCompatInsetsDispatch(viewGroup);
        }

        InsetsPadding basePadding = (InsetsPadding) view.getTag(R.id.tag_edge_to_edge_padding);
        if (basePadding == null) {
            basePadding = new InsetsPadding(
                    view.getPaddingStart(),
                    view.getPaddingTop(),
                    view.getPaddingEnd(),
                    view.getPaddingBottom()
            );
            view.setTag(R.id.tag_edge_to_edge_padding, basePadding);
        }

        InsetsPadding padding = basePadding;
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(insetTypes);
            int start = applyStart ? insets.left : 0;
            int top = applyTop ? insets.top : 0;
            int end = applyEnd ? insets.right : 0;
            int bottom = applyBottom ? insets.bottom : 0;

            ViewCompat.setPaddingRelative(
                    v,
                    padding.start + start,
                    padding.top + top,
                    padding.end + end,
                    padding.bottom + bottom
            );
            return windowInsets;
        });

        requestApplyInsetsWhenAttached(view);
    }

    private static void requestApplyInsetsWhenAttached(@NonNull View view) {
        if (ViewCompat.isAttachedToWindow(view)) {
            ViewCompat.requestApplyInsets(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {
                    v.removeOnAttachStateChangeListener(this);
                    ViewCompat.requestApplyInsets(v);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    // No-op
                }
            });
        }
    }

    private static final class InsetsPadding {
        final int start;
        final int top;
        final int end;
        final int bottom;

        InsetsPadding(int start, int top, int end, int bottom) {
            this.start = start;
            this.top = top;
            this.end = end;
            this.bottom = bottom;
        }
    }
}
