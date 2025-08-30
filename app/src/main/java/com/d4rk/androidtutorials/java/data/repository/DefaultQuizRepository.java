package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;
import com.d4rk.androidtutorials.java.data.source.QuizLocalDataSource;

import java.util.List;

/**
 * Default implementation of {@link QuizRepository} using a local data source.
 */
public class DefaultQuizRepository implements QuizRepository {

    private final QuizLocalDataSource localDataSource;

    public DefaultQuizRepository(QuizLocalDataSource localDataSource) {
        this.localDataSource = localDataSource;
    }

    @Override
    public List<QuizQuestion> loadQuestions() {
        return localDataSource.loadQuestions();
    }
}
