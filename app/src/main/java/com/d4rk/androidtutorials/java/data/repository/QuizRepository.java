package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;

import java.util.List;

/**
 * Abstraction over quiz data operations.
 */
public interface QuizRepository {

    interface QuestionsCallback {
        void onResult(List<QuizQuestion> questions);
    }

    void loadQuestions(QuestionsCallback callback);
}
