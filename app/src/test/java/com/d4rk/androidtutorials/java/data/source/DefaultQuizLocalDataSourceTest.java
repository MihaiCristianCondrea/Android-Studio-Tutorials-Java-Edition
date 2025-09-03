package com.d4rk.androidtutorials.java.data.source;

import android.content.res.AssetManager;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class DefaultQuizLocalDataSourceTest {

    private static class FakeAssetManager extends AssetManager {
        private final InputStream stream;
        FakeAssetManager(String json) {
            this.stream = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        }
        @Override
        public InputStream open(String fileName) throws IOException {
            return stream;
        }
    }

    private static class ErrorAssetManager extends AssetManager {
        @Override
        public InputStream open(String fileName) throws IOException {
            throw new IOException("missing");
        }
    }

    private static class ImmediateExecutor extends AbstractExecutorService {
        @Override public void shutdown() {}
        @Override public List<Runnable> shutdownNow() { return Collections.emptyList(); }
        @Override public boolean isShutdown() { return false; }
        @Override public boolean isTerminated() { return false; }
        @Override public boolean awaitTermination(long timeout, TimeUnit unit) { return true; }
        @Override public void execute(Runnable command) { command.run(); }
    }

    @Test
    public void loadQuestions_parsesValidFile() {
        String json = "[{\"question\":\"Q1\",\"options\":[\"A\",\"B\"],\"answer\":0},{\"question\":\"Q2\",\"options\":[\"C\",\"D\"],\"answer\":1}]";
        DefaultQuizLocalDataSource source = new DefaultQuizLocalDataSource(new FakeAssetManager(json), new ImmediateExecutor());
        final List<QuizQuestion>[] result = new List[1];
        source.loadQuestions(q -> result[0] = q);

        List<QuizQuestion> questions = result[0];
        assertNotNull(questions);
        assertEquals(2, questions.size());
        QuizQuestion first = questions.get(0);
        assertEquals("Q1", first.question());
        assertArrayEquals(new String[]{"A","B"}, first.options());
        assertEquals(0, first.answerIndex());
    }

    @Test
    public void loadQuestions_errorReturnsEmptyList() {
        DefaultQuizLocalDataSource source = new DefaultQuizLocalDataSource(new ErrorAssetManager(), new ImmediateExecutor());
        final List<QuizQuestion>[] result = new List[1];
        source.loadQuestions(q -> result[0] = q);
        assertNotNull(result[0]);
        assertTrue(result[0].isEmpty());
    }
}
