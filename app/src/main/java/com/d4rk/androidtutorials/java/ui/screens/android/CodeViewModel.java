package com.d4rk.androidtutorials.java.ui.screens.android;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.domain.android.GetLessonUseCase;
import com.d4rk.androidtutorials.java.ui.screens.android.repository.LessonRepository;

public class CodeViewModel extends ViewModel {
    private final MutableLiveData<com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson> lesson = new MutableLiveData<>();
    private final GetLessonUseCase getLessonUseCase;

    public CodeViewModel() {
        this(new GetLessonUseCase(new LessonRepository()));
    }

    CodeViewModel(GetLessonUseCase getLessonUseCase) {
        this.getLessonUseCase = getLessonUseCase;
    }

    public void setLessonName(String lessonName) {
        lesson.setValue(getLessonUseCase.invoke(lessonName));
    }

    public LiveData<com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson> getLesson() {
        return lesson;
    }
}
