package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.chronometer;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityChronometerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class ChronometerActivity extends SyntaxFabActivity {
    private ActivityChronometerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChronometerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_chronometer,
                true);

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "Chronometer");
            startActivity(intent);
        });
        binding.buttonStart.setOnClickListener(v -> binding.chronometer.start());
        binding.buttonStop.setOnClickListener(v -> binding.chronometer.stop());
        binding.buttonReset.setOnClickListener(v -> binding.chronometer.setBase(SystemClock.elapsedRealtime()));
    }
}
