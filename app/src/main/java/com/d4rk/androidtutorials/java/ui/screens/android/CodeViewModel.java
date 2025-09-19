package com.d4rk.androidtutorials.java.ui.screens.android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.data.repository.LessonRepository;
import com.d4rk.androidtutorials.java.domain.android.GetLessonUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class CodeViewModel extends ViewModel {
    private final MutableLiveData<LessonRepository.Lesson> lesson = new MutableLiveData<>();
    private final GetLessonUseCase getLessonUseCase;

    @Inject
    public CodeViewModel(GetLessonUseCase getLessonUseCase) {
        this.getLessonUseCase = getLessonUseCase;
    }

    public void setLessonName(String lessonName) {
        lesson.setValue(getLessonUseCase.invoke(lessonName));
    }

    public LiveData<LessonRepository.Lesson> getLesson() {
        return lesson;
    }
}
