package com.d4rk.androidtutorials.java.utils;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.LinkedHashMap;
import java.util.Map;

public class FontManagerTest {

    @Test
    public void getMonospaceFont_returnsExpectedTypefaceForStoredValues() {
        Context context = mock(Context.class);
        when(context.getString(R.string.key_monospace_font)).thenReturn("monospace_font");

        Map<String, Integer> values = new LinkedHashMap<>();
        values.put("0", R.font.font_audiowide);
        values.put("1", R.font.font_fira_code);
        values.put("2", R.font.font_jetbrains_mono);
        values.put("3", R.font.font_noto_sans_mono);
        values.put("4", R.font.font_poppins);
        values.put("5", R.font.font_roboto_mono);
        values.put("6", R.font.font_google_sans_code);

        for (Map.Entry<String, Integer> entry : values.entrySet()) {
            SharedPreferences prefs = mock(SharedPreferences.class);
            when(prefs.getString("monospace_font", "6")).thenReturn(entry.getKey());
            Typeface expectedTypeface = mock(Typeface.class);

            try (MockedStatic<ResourcesCompat> resourcesStatic = Mockito.mockStatic(ResourcesCompat.class)) {
                resourcesStatic.when(() -> ResourcesCompat.getFont(context, entry.getValue()))
                        .thenReturn(expectedTypeface);

                Typeface result = FontManager.getMonospaceFont(context, prefs);

                assertSame("Unexpected typeface for value " + entry.getKey(), expectedTypeface, result);
                verify(prefs).getString("monospace_font", "6");
                resourcesStatic.verify(() -> ResourcesCompat.getFont(context, entry.getValue()));
            }
        }
    }

    @Test
    public void getMonospaceFont_removesInvalidPreferenceValueAndReturnsDefaultFont() {
        Context context = mock(Context.class);
        SharedPreferences prefs = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);

        when(context.getString(R.string.key_monospace_font)).thenReturn("monospace_font");
        when(prefs.edit()).thenReturn(editor);
        when(editor.remove("monospace_font")).thenReturn(editor);
        when(prefs.getString("monospace_font", "6")).thenThrow(new ClassCastException("Expected string"));

        Typeface defaultTypeface = mock(Typeface.class);

        try (MockedStatic<ResourcesCompat> resourcesStatic = Mockito.mockStatic(ResourcesCompat.class)) {
            resourcesStatic.when(() -> ResourcesCompat.getFont(context, R.font.font_google_sans_code))
                    .thenReturn(defaultTypeface);

            Typeface result = FontManager.getMonospaceFont(context, prefs);

            assertSame(defaultTypeface, result);
            verify(prefs).getString("monospace_font", "6");
            verify(prefs).edit();
            InOrder inOrder = inOrder(editor);
            inOrder.verify(editor).remove("monospace_font");
            inOrder.verify(editor).apply();
            resourcesStatic.verify(() -> ResourcesCompat.getFont(context, R.font.font_google_sans_code));
        }
    }
}
