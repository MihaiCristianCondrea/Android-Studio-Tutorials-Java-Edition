package com.d4rk.androidtutorials.java.data.repository;

public interface LessonRepository {
    record Lesson(int titleResId, int codeResId, int layoutResId) {}
    Lesson getLesson(String lessonName);
}
