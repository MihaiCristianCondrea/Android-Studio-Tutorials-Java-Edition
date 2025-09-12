package com.d4rk.androidtutorials.java.data.model;

/**
 * Model representing a multiple-choice quiz question.
 */
public record QuizQuestion(
        String question,
        String[] options,
        int answerIndex
) {
}
