package com.d4rk.androidtutorials.java.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.MarginLayoutParamsCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewGroupCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.d4rk.androidtutorials.java.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public final class EdgeToEdgeDelegate {

    private EdgeToEdgeDelegate() {
        // Utility class
    }

    public static void apply(Activity activity, View view) {
        enableEdgeToEdge(activity);
        applyInsetsInternal(
                view,
                null,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
        );
    }

    public static void applyBottomBar(Activity activity, View container, View bottomNavigationView) {
        enableEdgeToEdge(activity);
        applyInsetsInternal(
                container,
                bottomNavigationView,
                WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout()
        );
    }

    private static void applyInsetsInternal(View root, @Nullable View explicitBottomBar, int insetTypes) {
        if (root == null) {
            return;
        }

        View topBar = null;
        View bottomBar = explicitBottomBar;
        if (root instanceof ViewGroup viewGroup) {
            topBar = findTopBar(viewGroup);
            if (bottomBar == null) {
                bottomBar = findBottomBar(viewGroup);
            }
            applyInsetsToFloatingButtons(viewGroup, insetTypes);
        }

        if (topBar != null && topBar != root) {
            applyInsetsAsPadding(topBar, insetTypes, true, false);
        }
        if (bottomBar != null && bottomBar != root) {
            applyInsetsAsPadding(bottomBar, insetTypes, false, true);
        }

        boolean applyTop = topBar == null || topBar == root;
        boolean applyBottom = bottomBar == null || bottomBar == root;
        applyInsetsAsPadding(root, insetTypes, applyTop, applyBottom);
    }

    private static void enableEdgeToEdge(Activity activity) {
        if (activity == null) {
            return;
        }
        WindowCompat.enableEdgeToEdge(activity.getWindow());
    }

    @Nullable
    private static View findTopBar(View view) {
        if (isVisible(view)) {
            return null;
        }
        if (isTopBar(view)) {
            return view;
        }
        if (view instanceof ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                View topBar = findTopBar(child);
                if (topBar != null) {
                    return topBar;
                }
            }
        }
        return null;
    }

    @Nullable
    private static View findBottomBar(View view) {
        if (isVisible(view)) {
            return null;
        }
        if (isBottomBar(view)) {
            return view;
        }
        if (view instanceof ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                View bottomBar = findBottomBar(child);
                if (bottomBar != null) {
                    return bottomBar;
                }
            }
        }
        return null;
    }

    private static void applyInsetsToFloatingButtons(View view, int insetTypes) {
        if (isVisible(view)) {
            return;
        }
        if (view instanceof ExtendedFloatingActionButton || view instanceof FloatingActionButton) {
            applyInsetsAsMargin(view, insetTypes);
            return;
        }
        if (view instanceof ViewGroup viewGroup) {
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyInsetsToFloatingButtons(viewGroup.getChildAt(i), insetTypes);
            }
        }
    }

    private static boolean isTopBar(View view) {
        int id = view.getId();
        return view instanceof AppBarLayout
                || view instanceof Toolbar
                || id == R.id.app_bar_layout
                || id == R.id.toolbar
                || id == R.id.top_app_bar;
    }

    private static boolean isBottomBar(View view) {
        int id = view.getId();
        return view instanceof NavigationBarView
                || view instanceof BottomAppBar
                || id == R.id.nav_view
                || id == R.id.bottom_nav
                || id == R.id.bottomBar;
    }

    private static boolean isVisible(View view) {
        return view.getVisibility() != View.VISIBLE;
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

    private static void applyInsetsAsMargin(View view, int insetTypes) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams marginLayoutParams)) {
            return;
        }

        InsetsMargin baseMargin = (InsetsMargin) view.getTag(R.id.tag_edge_to_edge_margin);
        if (baseMargin == null) {
            baseMargin = new InsetsMargin(
                    MarginLayoutParamsCompat.getMarginStart(marginLayoutParams),
                    marginLayoutParams.topMargin,
                    MarginLayoutParamsCompat.getMarginEnd(marginLayoutParams),
                    marginLayoutParams.bottomMargin
            );
            view.setTag(R.id.tag_edge_to_edge_margin, baseMargin);
        }

        InsetsMargin margin = baseMargin;
        ViewCompat.setOnApplyWindowInsetsListener(view, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(insetTypes);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            MarginLayoutParamsCompat.setMarginStart(lp, margin.start + insets.left);
            lp.topMargin = margin.top;
            MarginLayoutParamsCompat.setMarginEnd(lp, margin.end + insets.right);
            lp.bottomMargin = margin.bottom + (insets.bottom);
            v.setLayoutParams(lp);
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

    private record InsetsMargin(int start, int top, int end, int bottom) {
    }
}
