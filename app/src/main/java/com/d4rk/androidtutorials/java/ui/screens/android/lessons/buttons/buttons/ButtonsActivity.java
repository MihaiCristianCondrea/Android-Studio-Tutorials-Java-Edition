package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityButtonsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.material.snackbar.Snackbar;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ButtonsActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate.apply(this, binding.constraintLayout);

        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_buttons);
        AdUtils.loadBanner(binding.descriptionSection.adView);

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
        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(new Intent(ButtonsActivity.this, ButtonsCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
