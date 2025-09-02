package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.source.QuizLocalDataSource;

/**
 * Default implementation of {@link QuizRepository} using a local data source.
 */
public class DefaultQuizRepository implements QuizRepository {

    private final QuizLocalDataSource localDataSource;

    public DefaultQuizRepository(QuizLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    @Override
    public void loadQuestions(QuestionsCallback callback) {
        localDataSource.loadQuestions(callback::onResult);
    }
}
