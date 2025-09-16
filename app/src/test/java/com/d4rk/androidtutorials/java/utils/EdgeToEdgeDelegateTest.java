package com.d4rk.androidtutorials.java.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;
import org.mockito.MockedStatic;

public class EdgeToEdgeDelegateTest {

    @Test
    public void apply_setsDecorFitsFalseAndAppliesContainerPadding() {
        Activity activity = mock(Activity.class);
        Window window = mock(Window.class);
        when(activity.getWindow()).thenReturn(window);
        View container = mock(View.class);
        WindowInsetsCompat windowInsets = mock(WindowInsetsCompat.class);
        Insets insets = Insets.of(16, 32, 48, 64);
        when(windowInsets.getInsets(anyInt())).thenReturn(insets);

        AtomicReference<ViewCompat.OnApplyWindowInsetsListener> listenerRef = new AtomicReference<>();

        try (MockedStatic<WindowCompat> windowCompat = mockStatic(WindowCompat.class);
             MockedStatic<ViewCompat> viewCompat = mockStatic(ViewCompat.class)) {
            viewCompat.when(() -> ViewCompat.setOnApplyWindowInsetsListener(eq(container), any(ViewCompat.OnApplyWindowInsetsListener.class)))
                    .thenAnswer(invocation -> {
                        ViewCompat.OnApplyWindowInsetsListener listener = invocation.getArgument(1);
                        listenerRef.set(listener);
                        return null;
                    });

            EdgeToEdgeDelegate.apply(activity, container);

            windowCompat.verify(() -> WindowCompat.setDecorFitsSystemWindows(window, false));
            viewCompat.verify(() -> ViewCompat.setOnApplyWindowInsetsListener(eq(container), any(ViewCompat.OnApplyWindowInsetsListener.class)));

            ViewCompat.OnApplyWindowInsetsListener listener = listenerRef.get();
            assertNotNull(listener);

            WindowInsetsCompat result = listener.onApplyWindowInsets(container, windowInsets);
            int mask = WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout();

            verify(windowInsets).getInsets(mask);
            verify(container).setPadding(insets.left, insets.top, insets.right, insets.bottom);
            assertSame(WindowInsetsCompat.CONSUMED, result);
        }
    }

    @Test
    public void applyBottomBar_setsDecorFitsFalseAndAppliesContainerAndBottomPadding() {
        Activity activity = mock(Activity.class);
        Window window = mock(Window.class);
        when(activity.getWindow()).thenReturn(window);
        View container = mock(View.class);
        View bottomNavigationView = mock(View.class);
        WindowInsetsCompat windowInsets = mock(WindowInsetsCompat.class);
        Insets insets = Insets.of(8, 16, 24, 32);
        when(windowInsets.getInsets(anyInt())).thenReturn(insets);

        AtomicReference<ViewCompat.OnApplyWindowInsetsListener> listenerRef = new AtomicReference<>();

        try (MockedStatic<WindowCompat> windowCompat = mockStatic(WindowCompat.class);
             MockedStatic<ViewCompat> viewCompat = mockStatic(ViewCompat.class)) {
            viewCompat.when(() -> ViewCompat.setOnApplyWindowInsetsListener(eq(container), any(ViewCompat.OnApplyWindowInsetsListener.class)))
                    .thenAnswer(invocation -> {
                        ViewCompat.OnApplyWindowInsetsListener listener = invocation.getArgument(1);
                        listenerRef.set(listener);
                        return null;
                    });

            EdgeToEdgeDelegate.applyBottomBar(activity, container, bottomNavigationView);

            windowCompat.verify(() -> WindowCompat.setDecorFitsSystemWindows(window, false));
            viewCompat.verify(() -> ViewCompat.setOnApplyWindowInsetsListener(eq(container), any(ViewCompat.OnApplyWindowInsetsListener.class)));

            ViewCompat.OnApplyWindowInsetsListener listener = listenerRef.get();
            assertNotNull(listener);

            WindowInsetsCompat result = listener.onApplyWindowInsets(container, windowInsets);
            int mask = WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout();

            verify(windowInsets).getInsets(mask);
            verify(container).setPadding(insets.left, insets.top, insets.right, 0);
            verify(bottomNavigationView).setPadding(0, 0, 0, insets.bottom);
            assertSame(WindowInsetsCompat.CONSUMED, result);
        }
    }
}
