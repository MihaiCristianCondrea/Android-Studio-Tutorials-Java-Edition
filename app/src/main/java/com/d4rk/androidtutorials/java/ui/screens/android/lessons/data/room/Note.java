package com.d4rk.androidtutorials.java.ui.screens.android.lessons.data.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Simple Note entity stored in the Room database.
 */
@Entity(tableName = "notes")
public class Note {
    public final String text;
    @PrimaryKey(autoGenerate = true)
    public int id;

    public Note(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
