package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;
import com.d4rk.androidtutorials.java.data.source.QuizLocalDataSource;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class DefaultQuizRepositoryTest {

    private static class FakeQuizLocalDataSource implements QuizLocalDataSource {
        private final List<QuizQuestion> questions;

        FakeQuizLocalDataSource(List<QuizQuestion> questions) {
            this.questions = questions;
        }

        @Override
        public void loadQuestions(QuestionsCallback callback) {
            callback.onResult(questions);
        }
    }

    @Test
    public void loadQuestionsReturnsLocalData() throws InterruptedException {
        List<QuizQuestion> expected = List.of(
                new QuizQuestion("Q", new String[]{"A", "B"}, 0)
        );
        FakeQuizLocalDataSource local = new FakeQuizLocalDataSource(expected);
        DefaultQuizRepository repository = new DefaultQuizRepository(local);
        CountDownLatch latch = new CountDownLatch(1);
        repository.loadQuestions(result -> {
            assertEquals(expected, result);
            latch.countDown();
        });
        assertTrue(latch.await(1, TimeUnit.SECONDS));
    }
}
