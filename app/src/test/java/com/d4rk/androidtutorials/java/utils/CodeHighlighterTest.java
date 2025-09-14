package com.d4rk.androidtutorials.java.utils;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.amrdeveloper.codeview.CodeView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.Test;

/**
 * Tests for {@link CodeHighlighter} ensuring themes register syntax patterns and refresh highlighting.
 */
public class CodeHighlighterTest {

    @Test
    public void applyJavaTheme_addsPatternsAndReHighlights() {
        CodeView codeView = mock(CodeView.class);
        List<Pattern> patterns = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        doAnswer(invocation -> {
            patterns.add(invocation.getArgument(0));
            colors.add(invocation.getArgument(1));
            return null;
        }).when(codeView).addSyntaxPattern(any(Pattern.class), anyInt());

        CodeHighlighter.applyJavaTheme(codeView);

        assertEquals(5, patterns.size());
        assertEquals(5, colors.size());
        verify(codeView).reHighlightSyntax();
    }

    @Test
    public void applyXmlTheme_addsPatternsAndReHighlights() {
        CodeView codeView = mock(CodeView.class);
        List<Pattern> patterns = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        doAnswer(invocation -> {
            patterns.add(invocation.getArgument(0));
            colors.add(invocation.getArgument(1));
            return null;
        }).when(codeView).addSyntaxPattern(any(Pattern.class), anyInt());

        CodeHighlighter.applyXmlTheme(codeView);

        assertEquals(4, patterns.size());
        assertEquals(4, colors.size());
        verify(codeView).reHighlightSyntax();
    }
}
