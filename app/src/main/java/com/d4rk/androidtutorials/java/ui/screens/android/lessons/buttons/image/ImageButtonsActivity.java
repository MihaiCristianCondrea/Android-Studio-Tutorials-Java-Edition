package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.image;

import android.content.Intent;
import android.os.Bundle;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityImageButtonsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.snackbar.Snackbar;

public class ImageButtonsActivity extends SyntaxFabActivity {
    private ActivityImageButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_image_buttons,
                true);

        binding.imageButton.setOnClickListener(v ->
                Snackbar.make(binding.getRoot(), R.string.snack_image_button, Snackbar.LENGTH_SHORT).show()
        );

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "ImageButtons");
            startActivity(intent);
        });
    }
}
