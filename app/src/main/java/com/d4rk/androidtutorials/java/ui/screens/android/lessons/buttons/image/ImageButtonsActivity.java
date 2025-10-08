package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.image;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityImageButtonsBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.snackbar.Snackbar;

public class ImageButtonsActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityImageButtonsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImageButtonsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_image_buttons);
        AdUtils.loadBanner(binding.descriptionSection.adView);

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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
