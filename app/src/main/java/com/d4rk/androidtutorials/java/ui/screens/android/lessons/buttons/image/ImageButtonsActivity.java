package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.image;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityImageButtonsBinding;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.material.snackbar.Snackbar;

public class ImageButtonsActivity extends UpNavigationActivity {
    private final Handler handler = new Handler();
    private ActivityImageButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        binding.imageButton.setOnClickListener(v ->
                Snackbar.make(binding.getRoot(), R.string.snack_image_button, Snackbar.LENGTH_SHORT).show()
        );

        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "ImageButtons");
            startActivity(intent);
        });

        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
}