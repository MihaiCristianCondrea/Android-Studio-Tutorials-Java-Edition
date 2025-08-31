package com.d4rk.androidtutorials.java.ui.screens.android.lessons.textboxes.textbox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import com.d4rk.androidtutorials.java.databinding.ActivityTextBoxBinding;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.google.android.material.snackbar.Snackbar;

public class TextboxActivity extends UpNavigationActivity {
    private final Handler handler = new Handler();
    private ActivityTextBoxBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTextBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindListeners();
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }

    private void bindListeners() {
        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "TextBox");
            startActivity(intent);
        });
        addKeyListener();
    }

    private void addKeyListener() {
        binding.buttonPrintEdit.setOnClickListener(v -> {
            CharSequence text = binding.editText.getText();
            if (text != null) {
                Snackbar.make(binding.getRoot(), text, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}