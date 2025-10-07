package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.switches;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivitySwitchBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.snackbar.Snackbar;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class SwitchActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivitySwitchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySwitchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_toggle);
        AdUtils.loadBanner(binding.descriptionSection.adView);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        binding.materialSwitchPreference.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.material_switch_preference, Snackbar.LENGTH_SHORT).show());
        binding.materialSwitch.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.material_switch, Snackbar.LENGTH_SHORT).show());
        binding.switchMaterial.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.switch_material, Snackbar.LENGTH_SHORT).show());
        binding.buttonToggle.setOnClickListener(view -> Snackbar.make(binding.getRoot(), R.string.toggle_button, Snackbar.LENGTH_SHORT).show());
        binding.floatingButtonShowSyntax.setOnClickListener(view -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "Switch");
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
