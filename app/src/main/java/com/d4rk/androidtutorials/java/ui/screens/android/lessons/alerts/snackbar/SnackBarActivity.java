package com.d4rk.androidtutorials.java.ui.screens.android.lessons.alerts.snackbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivitySnackBarBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.snackbar.Snackbar;

public class SnackBarActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivitySnackBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySnackBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.button.setOnClickListener(v -> {
            Snackbar snackbar = Snackbar.make(binding.getRoot(), R.string.snack_this_is_a_snackbar, Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction(android.R.string.ok, v1 -> snackbar.dismiss());
            snackbar.show();
        });

        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "SnackBar");
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
