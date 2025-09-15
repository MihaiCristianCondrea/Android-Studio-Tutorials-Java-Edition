package com.d4rk.androidtutorials.java.ui.screens.android;

import static org.junit.Assert.assertEquals;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.data.repository.LessonRepository;
import com.d4rk.androidtutorials.java.domain.android.GetLessonUseCase;

import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class CodeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void setLessonName_updatesLiveData() {
        FakeLessonRepository repository = new FakeLessonRepository();
        LessonRepository.Lesson expected = new LessonRepository.Lesson(1, 2, 3);
        repository.addLesson("lesson", expected);
        CodeViewModel viewModel = new CodeViewModel(new GetLessonUseCase(repository));

        viewModel.setLessonName("lesson");

        assertEquals(expected, viewModel.getLesson().getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setLessonName_unknownLessonThrows() {
        FakeLessonRepository repository = new FakeLessonRepository();
        CodeViewModel viewModel = new CodeViewModel(new GetLessonUseCase(repository));

        viewModel.setLessonName("unknown");
    }

    private static final class FakeLessonRepository implements LessonRepository {
        private final Map<String, Lesson> lessons = new HashMap<>();

        void addLesson(String name, Lesson lesson) {
            lessons.put(name, lesson);
        }

        @Override
        public Lesson getLesson(String lessonName) {
            Lesson lesson = lessons.get(lessonName);
            if (lesson == null) {
                throw new IllegalArgumentException("Unknown lesson: " + lessonName);
            }
            return lesson;
        }
    }
}
