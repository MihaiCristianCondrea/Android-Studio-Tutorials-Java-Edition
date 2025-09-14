package com.d4rk.androidtutorials.java.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.graphics.Typeface;

import com.amrdeveloper.codeview.CodeView;

import org.junit.Test;

public class CodeViewUtilsTest {

    private void verifyDefaults(CodeView view, Typeface typeface) {
        verify(view).setTypeface(typeface);
        verify(view).setTextSize(14f);
        verify(view).setLineNumberTextSize(14f);
        verify(view).setEnableLineNumber(false);
        verify(view).setHorizontallyScrolling(false);
        verify(view).setKeyListener(null);
        verify(view).setCursorVisible(false);
        verify(view).setTextIsSelectable(true);
        verify(view).setHorizontalScrollBarEnabled(false);
        verify(view).setVerticalScrollBarEnabled(false);
        verify(view).setBackground(null);
    }

    @Test
    public void applyDefaults_configuresAllViews() {
        Typeface typeface = mock(Typeface.class);
        CodeView first = mock(CodeView.class);
        CodeView second = mock(CodeView.class);

        CodeViewUtils.applyDefaults(typeface, first, second, null);

        verifyDefaults(first, typeface);
        verifyDefaults(second, typeface);
    }
}

