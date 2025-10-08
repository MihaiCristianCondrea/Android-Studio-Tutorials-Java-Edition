package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.switches;

import android.content.Intent;
import android.os.Bundle;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivitySwitchBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.snackbar.Snackbar;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class SwitchActivity extends SyntaxFabActivity {
    private ActivitySwitchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwitchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_toggle,
                true);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.materialSwitchPreference.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.material_switch_preference, Snackbar.LENGTH_SHORT).show());
        binding.materialSwitch.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.material_switch, Snackbar.LENGTH_SHORT).show());
        binding.switchMaterial.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.switch_material, Snackbar.LENGTH_SHORT).show());
        binding.buttonToggle.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.toggle_button, Snackbar.LENGTH_SHORT).show());
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "Switch");
            startActivity(intent);
        });
    }
}
