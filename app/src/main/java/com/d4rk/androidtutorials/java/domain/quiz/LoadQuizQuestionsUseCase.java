package com.d4rk.androidtutorials.java.domain.quiz;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;
import com.d4rk.androidtutorials.java.data.repository.QuizRepository;

import java.util.List;

/**
 * Loads quiz questions from assets.
 */
public class LoadQuizQuestionsUseCase {
    private final QuizRepository repository;

    public LoadQuizQuestionsUseCase(QuizRepository repository) {
        this.repository = repository;
    }

    public void invoke(Callback callback) {
        repository.loadQuestions(callback::onResult);
    }

    public interface Callback {
        void onResult(List<QuizQuestion> questions);
    }
}
