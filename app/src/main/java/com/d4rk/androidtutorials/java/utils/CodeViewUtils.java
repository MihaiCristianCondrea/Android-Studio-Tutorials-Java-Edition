package com.d4rk.androidtutorials.java.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.util.Log;
import android.view.ViewParent;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.preference.PreferenceManager;

import com.amrdeveloper.codeview.CodeView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import me.zhanghai.android.fastscroll.FastScrollScrollView;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

/**
 * Utility methods for configuring {@link CodeView} instances.
 */
public final class CodeViewUtils {

    private CodeViewUtils() {
        // Utility class.
    }

    /**
     * Highlighting modes supported by {@link #populateFromRawResource(CodeView, int, HighlightMode, String)}.
     */
    public enum HighlightMode {
        JAVA,
        XML
    }

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

    /**
     * Populate the provided {@link CodeView} with the contents of a raw resource file, applying
     * the shared font, fast-scroller and syntax highlighting configuration.
     *
     * @param codeView      the view to populate
     * @param rawResourceId the raw resource containing the code snippet
     * @param highlightMode the syntax highlighting mode to apply
     * @param logTag        tag used when logging errors
     * @return the loaded text, or an empty string if loading failed
     */
    @NonNull
    public static String populateFromRawResource(@NonNull CodeView codeView,
                                                 @RawRes int rawResourceId,
                                                 @NonNull HighlightMode highlightMode,
                                                 @NonNull String logTag) {
        Context context = codeView.getContext();

        attachFastScroller(codeView);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Typeface monospaceFont = FontManager.getMonospaceFont(context, prefs);
        applyDefaults(monospaceFont, codeView);

        Resources resources = context.getResources();
        try (InputStream inputStream = resources.openRawResource(rawResourceId);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8))) { // explicit UTF-8
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append('\n');
            }
            String text = builder.toString();
            codeView.setText(text);
            applyHighlighting(codeView, highlightMode);
            return text;
        } catch (Resources.NotFoundException | IOException e) {
            Log.e(logTag, "Error loading raw resource: " + rawResourceId, e);
            codeView.setText("");
            return "";
        }
    }

    private static void applyHighlighting(CodeView codeView, HighlightMode highlightMode) {
        switch (highlightMode) {
            case JAVA:
                CodeHighlighter.applyJavaTheme(codeView);
                break;
            case XML:
                CodeHighlighter.applyXmlTheme(codeView);
                break;
            default:
                // no-op
        }
    }

    private static void attachFastScroller(CodeView codeView) {
        ViewParent parent = codeView.getParent();
        while (parent != null) {
            // Java 17 pattern variable: narrows and binds as ViewGroup
            if (parent instanceof FastScrollScrollView viewGroup) {
                // FastScrollerBuilder expects the actual scroll container (RecyclerView/ScrollView/etc.)
                new FastScrollerBuilder(viewGroup).useMd2Style().build();
                break;
            }

            parent = parent.getParent();
        }
    }
}
