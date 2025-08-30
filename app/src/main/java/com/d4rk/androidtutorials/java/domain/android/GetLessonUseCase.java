package com.d4rk.androidtutorials.java.domain.android;

import com.d4rk.androidtutorials.java.data.repository.LessonRepository;

/** Retrieves lesson data by name. */
public class GetLessonUseCase {
    private final LessonRepository repository;

    public GetLessonUseCase(LessonRepository repository) {
        this.repository = repository;
    }

    public LessonRepository.Lesson invoke(String lessonName) {
        return repository.getLesson(lessonName);
    }
}
