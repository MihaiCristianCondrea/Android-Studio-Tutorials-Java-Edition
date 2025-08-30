package com.d4rk.androidtutorials.java.data.source;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;

import java.util.List;

/**
 * Contract for reading quiz data from local storage.
 */
public interface QuizLocalDataSource {
    List<QuizQuestion> loadQuestions();
}
