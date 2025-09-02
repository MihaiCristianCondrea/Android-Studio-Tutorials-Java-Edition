package com.d4rk.androidtutorials.java.ui.screens.android.lessons.data.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Simple Note entity stored in the Room database.
 */
@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String text;

    public Note(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
