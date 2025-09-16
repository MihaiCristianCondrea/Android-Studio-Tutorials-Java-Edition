package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;

public class LessonRepositoryTest {

    private final com.d4rk.androidtutorials.java.ui.screens.android.repository.LessonRepository repository =
            new com.d4rk.androidtutorials.java.ui.screens.android.repository.LessonRepository();

    @Test
    public void getLesson_knownLessonReturnsRecord() {
        LessonRepository.Lesson expected = new LessonRepository.Lesson(
                R.string.alert_dialog,
                R.raw.text_alertdialog_java,
                R.raw.text_center_button_xml
        );

        LessonRepository.Lesson result = repository.getLesson("AlertDialog");

        assertEquals(expected, result);
    }

    @Test
    public void getLesson_emptyNameThrows() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.getLesson("")
        );
        assertEquals("Unknown lesson: ", exception.getMessage());
    }

    @Test
    public void getLesson_unknownLessonThrows() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.getLesson("Unknown")
        );
        assertEquals("Unknown lesson: Unknown", exception.getMessage());
    }
}
