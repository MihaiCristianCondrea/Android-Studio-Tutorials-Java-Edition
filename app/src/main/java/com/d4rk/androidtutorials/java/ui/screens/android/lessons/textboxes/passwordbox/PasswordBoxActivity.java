package com.d4rk.androidtutorials.java.ui.screens.android.lessons.textboxes.passwordbox;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityPasswordBoxBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.google.android.material.snackbar.Snackbar;

public class PasswordBoxActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityPasswordBoxBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPasswordBoxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bindListeners();
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }

    private void bindListeners() {
        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "PasswordBox");
            startActivity(intent);
        });
        binding.showPasswordButton.setOnClickListener(v ->
                togglePasswordVisibility());
        addKeyListener();
    }

    private void togglePasswordVisibility() {
        if (binding.showPasswordButton.getText().equals("Show")) {
            showPassword();
        } else {
            hidePassword();
        }
    }

    @SuppressLint("SetTextI18n")
    private void showPassword() {
        binding.editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        binding.showPasswordButton.setIconResource(R.drawable.ic_visible_off);
        binding.showPasswordButton.setText("Hide");
    }

    @SuppressLint("SetTextI18n")
    private void hidePassword() {
        binding.editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        binding.showPasswordButton.setIconResource(R.drawable.ic_visible);
        binding.showPasswordButton.setText("Show");
    }

    private void addKeyListener() {
        binding.buttonShowPassword.setOnClickListener(v ->
                Snackbar.make(binding.getRoot(), String.valueOf(binding.editText.getText()), Snackbar.LENGTH_LONG).show());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
