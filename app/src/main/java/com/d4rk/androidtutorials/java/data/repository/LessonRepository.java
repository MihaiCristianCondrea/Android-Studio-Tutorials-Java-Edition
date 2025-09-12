package com.d4rk.androidtutorials.java.data.repository;

public interface LessonRepository {
    Lesson getLesson(String lessonName);

    record Lesson(int titleResId, int codeResId, int layoutResId) {
    }
}
