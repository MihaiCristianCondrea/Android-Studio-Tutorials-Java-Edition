package com.d4rk.androidtutorials.java.domain.android;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.d4rk.androidtutorials.java.data.repository.LessonRepository;

import org.junit.Test;

public class GetLessonUseCaseTest {

    @Test
    public void invokeReturnsLesson() {
        LessonRepository repository = mock(LessonRepository.class);
        LessonRepository.Lesson lesson = new LessonRepository.Lesson(1, 2, 3);
        when(repository.getLesson("intro")).thenReturn(lesson);
        GetLessonUseCase useCase = new GetLessonUseCase(repository);

        LessonRepository.Lesson result = useCase.invoke("intro");

        assertSame(lesson, result);
        verify(repository).getLesson("intro");
    }
}

