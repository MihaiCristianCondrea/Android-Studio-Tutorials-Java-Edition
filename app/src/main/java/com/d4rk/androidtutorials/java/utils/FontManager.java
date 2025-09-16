package com.d4rk.androidtutorials.java.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

import com.d4rk.androidtutorials.java.R;

public class FontManager {

    private static final String DEFAULT_FONT_CODE = "6";

    public static Typeface getMonospaceFont(Context context, SharedPreferences prefs) {
        String key = context.getString(R.string.key_monospace_font);
        String font;
        boolean resetPreference = false;
        try {
            font = prefs.getString(key, DEFAULT_FONT_CODE);
        } catch (ClassCastException e) {
            font = DEFAULT_FONT_CODE;
            resetPreference = true;
        }
        if (font == null) {
            font = DEFAULT_FONT_CODE;
            resetPreference = true;
        }

        Typeface typeface;
        switch (font) {
            case "0":
                typeface = ResourcesCompat.getFont(context, R.font.font_audiowide);
                break;
            case "1":
                typeface = ResourcesCompat.getFont(context, R.font.font_fira_code);
                break;
            case "2":
                typeface = ResourcesCompat.getFont(context, R.font.font_jetbrains_mono);
                break;
            case "3":
                typeface = ResourcesCompat.getFont(context, R.font.font_noto_sans_mono);
                break;
            case "4":
                typeface = ResourcesCompat.getFont(context, R.font.font_poppins);
                break;
            case "5":
                typeface = ResourcesCompat.getFont(context, R.font.font_roboto_mono);
                break;
            case DEFAULT_FONT_CODE:
                typeface = ResourcesCompat.getFont(context, R.font.font_google_sans_code);
                break;
            default:
                typeface = ResourcesCompat.getFont(context, R.font.font_google_sans_code);
                resetPreference = true;
                break;
        }

        if (resetPreference) {
            prefs.edit().remove(key).apply();
        }

        return typeface;
    }
}
