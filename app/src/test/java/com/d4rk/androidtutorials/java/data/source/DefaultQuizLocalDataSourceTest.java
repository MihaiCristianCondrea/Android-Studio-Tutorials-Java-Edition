package com.d4rk.androidtutorials.java.data.source;

import android.content.res.AssetManager;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DefaultQuizLocalDataSourceTest {

    @Test
    public void loadQuestionsParsesFile() throws Exception {
        AssetManager manager = mock(AssetManager.class);
        String json = "[{\"question\":\"Q1\",\"options\":[\"A\",\"B\"],\"answer\":1},{\"question\":\"Q2\",\"options\":[\"C\",\"D\",\"E\"],\"answer\":0}]";
        when(manager.open("quiz_questions.json")).thenReturn(
                new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)));

        DefaultQuizLocalDataSource dataSource = new DefaultQuizLocalDataSource(manager);
        List<QuizQuestion> result = dataSource.loadQuestions();

        assertEquals(2, result.size());
        QuizQuestion first = result.get(0);
        assertEquals("Q1", first.question());
        assertArrayEquals(new String[]{"A", "B"}, first.options());
        assertEquals(1, first.answerIndex());
    }

    @Test
    public void loadQuestionsReturnsEmptyOnError() throws Exception {
        AssetManager manager = mock(AssetManager.class);
        when(manager.open("quiz_questions.json")).thenThrow(new IOException("missing"));

        DefaultQuizLocalDataSource dataSource = new DefaultQuizLocalDataSource(manager);
        List<QuizQuestion> result = dataSource.loadQuestions();

        assertTrue(result.isEmpty());
    }
}
