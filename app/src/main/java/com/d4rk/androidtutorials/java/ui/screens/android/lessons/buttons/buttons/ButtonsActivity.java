package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityButtonsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons.tabs.ButtonsTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons.tabs.ButtonsTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ButtonsActivity extends SyntaxFabActivity {
    private ActivityButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_buttons,
                true);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.buttonNormal.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.button_normal) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.buttonOutlined.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.button_outlined) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.buttonElevated.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.button_elevated) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.buttonNormalIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.button_normal_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.buttonOutlinedIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.button_outlined_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.buttonElevatedIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.button_elevated_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonPrimary.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_primary) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonSecondary.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_secondary) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonSurface.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_surface) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonTertiary.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_tertiary) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonPrimaryIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_primary_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonSecondaryIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_secondary_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonSurfaceIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_surface_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.extendedFloatingButtonTertiaryIcon.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.extended_floating_button_tertiary_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.floatingButtonPrimary.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.floating_button_primary_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.floatingButtonSecondary.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.floating_button_secondary_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.floatingButtonSurface.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.floating_button_surface_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        binding.floatingButtonTertiary.setOnClickListener(view -> Snackbar.make(binding.getRoot(), getString(R.string.floating_button_tertiary_icon) + " " + getString(R.string.snack_bar_clicked), Snackbar.LENGTH_SHORT).show());
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        ButtonsActivity.this,
                        R.string.buttons,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        ButtonsTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        ButtonsTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }
}
