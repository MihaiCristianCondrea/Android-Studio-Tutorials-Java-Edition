package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.radio;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;

import com.d4rk.androidtutorials.java.databinding.ActivityRadioButtonsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;

public class RadioButtonsActivity extends SyntaxFabActivity {
    private ActivityRadioButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRadioButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        SparseArray<MaterialRadioButton> radioButtons = new SparseArray<>();
        radioButtons.put(binding.radioButtonFirstOption.getId(), binding.radioButtonFirstOption);
        radioButtons.put(binding.radioButtonSecondOption.getId(), binding.radioButtonSecondOption);

        binding.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            MaterialRadioButton radioButton = radioButtons.get(checkedId);
            if (radioButton != null) {
                Snackbar.make(binding.getRoot(), radioButton.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "RadioButtons");
            startActivity(intent);
        });
    }
}
