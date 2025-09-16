package com.d4rk.androidtutorials.java.ui.screens.android;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.domain.android.GetLessonUseCase;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CodeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetLessonUseCase getLessonUseCase;

    @Test
    public void initialLesson_isNull() {
        CodeViewModel viewModel = new CodeViewModel(getLessonUseCase);

        assertNull(viewModel.getLesson().getValue());
    }

    @Test
    public void setLessonName_updatesLiveData() {
        com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson lesson =
                new com.d4rk.androidtutorials.java.data.repository.LessonRepository.Lesson(1, 2, 3);
        when(getLessonUseCase.invoke("lesson")).thenReturn(lesson);

        CodeViewModel viewModel = new CodeViewModel(getLessonUseCase);

        viewModel.setLessonName("lesson");

        assertEquals(lesson, viewModel.getLesson().getValue());
        verify(getLessonUseCase).invoke("lesson");
    }

    @Test
    public void setLessonName_propagatesFailure() {
        when(getLessonUseCase.invoke("missing"))
                .thenThrow(new IllegalArgumentException("missing"));

        CodeViewModel viewModel = new CodeViewModel(getLessonUseCase);

        assertThrows(IllegalArgumentException.class, () -> viewModel.setLessonName("missing"));
    }
}

