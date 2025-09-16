package com.d4rk.androidtutorials.java.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import com.amrdeveloper.codeview.CodeView;

import org.junit.Test;
import org.mockito.InOrder;

import java.util.regex.Pattern;

/**
 * Tests for {@link CodeHighlighter}.
 */
public class CodeHighlighterTest {

    @Test
    public void applyJavaTheme_resetsPatternsAndReHighlights() {
        CodeView codeView = mock(CodeView.class);

        CodeHighlighter.applyJavaTheme(codeView);

        InOrder order = inOrder(codeView);
        order.verify(codeView).resetSyntaxPatternList();
        order.verify(codeView).resetHighlighter();
        order.verify(codeView, times(5)).addSyntaxPattern(any(Pattern.class), anyInt());
        order.verify(codeView).reHighlightSyntax();
    }

    @Test
    public void applyXmlTheme_resetsPatternsAndReHighlights() {
        CodeView codeView = mock(CodeView.class);

        CodeHighlighter.applyXmlTheme(codeView);

        InOrder order = inOrder(codeView);
        order.verify(codeView).resetSyntaxPatternList();
        order.verify(codeView).resetHighlighter();
        order.verify(codeView, times(4)).addSyntaxPattern(any(Pattern.class), anyInt());
        order.verify(codeView).reHighlightSyntax();
    }
}
