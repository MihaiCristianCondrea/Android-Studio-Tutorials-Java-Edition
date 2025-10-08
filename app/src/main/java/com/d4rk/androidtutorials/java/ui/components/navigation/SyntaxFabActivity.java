package com.d4rk.androidtutorials.java.ui.components.navigation;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

/**
 * Base activity that encapsulates the common pattern of wiring a syntax FAB that shrinks after
 * a short delay. A large portion of the lesson detail screens use the same behaviour which was
 * previously duplicated in every activity.
 */
public abstract class SyntaxFabActivity extends UpNavigationActivity {

    private static final long DEFAULT_SHRINK_DELAY_MS = 5000L;

    private final Handler syntaxFabHandler = new Handler(Looper.getMainLooper());

    protected final void setupSyntaxFab(@Nullable ExtendedFloatingActionButton fab,
                                        @NonNull Runnable onClick) {
        if (fab == null) {
            return;
        }

        fab.setOnClickListener(view -> onClick.run());
        syntaxFabHandler.postDelayed(fab::shrink, DEFAULT_SHRINK_DELAY_MS);
    }

    @Override
    protected void onDestroy() {
        syntaxFabHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
