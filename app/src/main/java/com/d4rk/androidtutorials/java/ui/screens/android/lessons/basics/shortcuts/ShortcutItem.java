package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts;

import androidx.annotation.StringRes;

/**
 * Represents a keyboard shortcut entry with its key combination and description.
 */
public record ShortcutItem(String key, @StringRes int descriptionResId) { }