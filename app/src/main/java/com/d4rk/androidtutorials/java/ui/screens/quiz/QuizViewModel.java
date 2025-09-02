package com.d4rk.androidtutorials.java.ui.screens.quiz;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.d4rk.androidtutorials.java.data.model.QuizQuestion;
import com.d4rk.androidtutorials.java.domain.quiz.LoadQuizQuestionsUseCase;

import java.util.Collections;
import java.util.List;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

/**
 * ViewModel managing quiz state and scoring.
 */
@HiltViewModel
public class QuizViewModel extends ViewModel {

    private final MutableLiveData<List<QuizQuestion>> questions = new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<Integer> currentIndex = new MutableLiveData<>(0);
    private final MutableLiveData<Integer> score = new MutableLiveData<>(0);
    private final LoadQuizQuestionsUseCase loadQuizQuestionsUseCase;

    @Inject
    public QuizViewModel(LoadQuizQuestionsUseCase loadQuizQuestionsUseCase) {
        this.loadQuizQuestionsUseCase = loadQuizQuestionsUseCase;
        loadQuizQuestionsUseCase.invoke(result -> questions.postValue(result));
    }

    public QuizQuestion getCurrentQuestion() {
        List<QuizQuestion> list = questions.getValue();
        if (list == null || list.isEmpty()) return null;
        int index = currentIndex.getValue();
        return list.get(Math.min(index, list.size() - 1));
    }

    public LiveData<Integer> getCurrentIndex() {
        return currentIndex;
    }

    public LiveData<Integer> getScore() {
        return score;
    }

    public LiveData<List<QuizQuestion>> getQuestions() {
        return questions;
    }

    public void answer(int optionIndex) {
        QuizQuestion question = getCurrentQuestion();
        if (question != null && optionIndex == question.answerIndex()) {
            score.setValue(score.getValue() + 1);
        }
        currentIndex.setValue(currentIndex.getValue() + 1);
    }

    public int getTotalQuestions() {
        List<QuizQuestion> list = questions.getValue();
        return list != null ? list.size() : 0;
    }
}
