package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.radio;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;

import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.databinding.ActivityRadioButtonsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;

public class RadioButtonsActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityRadioButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRadioButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowCompat.enableEdgeToEdge(getWindow());

        SparseArray<MaterialRadioButton> radioButtons = new SparseArray<>();
        radioButtons.put(binding.radioButtonFirstOption.getId(), binding.radioButtonFirstOption);
        radioButtons.put(binding.radioButtonSecondOption.getId(), binding.radioButtonSecondOption);

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            MaterialRadioButton radioButton = radioButtons.get(checkedId);
            if (radioButton != null) {
                Snackbar.make(binding.getRoot(), radioButton.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });

        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "RadioButtons");
            startActivity(intent);
        });

        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
