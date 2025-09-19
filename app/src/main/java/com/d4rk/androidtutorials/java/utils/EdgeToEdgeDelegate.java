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
                true
        );
    }

    public static void applyBottomBar(Activity activity, View container, View bottomNavigationView) {
        enableEdgeToEdge(activity);
        applyInsetsAsPadding(
                container,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                true,
                false
        );
        applyInsetsAsPadding(
                bottomNavigationView,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout(),
                false,
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
                                             boolean applyTop,
                                             boolean applyBottom) {
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
            int start = insets.left;
            int top = applyTop ? insets.top : 0;
            int end = insets.right;
            int bottom = applyBottom ? insets.bottom : 0;

            ViewCompat.setPaddingRelative( // FIXME: 'setPaddingRelative(android.view.@org.jspecify.annotations.NonNull View, int, int, int, int)' is deprecated
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
        if (ViewCompat.isAttachedToWindow(view)) { // FIXME: 'isAttachedToWindow(android.view.@org.jspecify.annotations.NonNull View)' is deprecated
            ViewCompat.requestApplyInsets(view);
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(@NonNull View v) {
                    v.removeOnAttachStateChangeListener(this);
                    ViewCompat.requestApplyInsets(v);
                }

                @Override
                public void onViewDetachedFromWindow(@NonNull View v) {
                    // No-op
                }
            });
        }
    }

    private record InsetsPadding(int start, int top, int end, int bottom) {
    }
}
