package com.d4rk.androidtutorials.java.domain.android;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
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

    @Test
    public void invokeReturnsNullWhenRepositoryReturnsNull() {
        LessonRepository repository = mock(LessonRepository.class);
        when(repository.getLesson("advanced")).thenReturn(null);
        GetLessonUseCase useCase = new GetLessonUseCase(repository);

        LessonRepository.Lesson result = useCase.invoke("advanced");

        assertNull(result);
        verify(repository).getLesson("advanced");
    }

    @Test
    public void invokePropagatesRepositoryException() {
        LessonRepository repository = mock(LessonRepository.class);
        when(repository.getLesson("intro")).thenThrow(new IllegalStateException("failure"));
        GetLessonUseCase useCase = new GetLessonUseCase(repository);

        assertThrows(IllegalStateException.class, () -> useCase.invoke("intro"));
        verify(repository).getLesson("intro");
    }
}

