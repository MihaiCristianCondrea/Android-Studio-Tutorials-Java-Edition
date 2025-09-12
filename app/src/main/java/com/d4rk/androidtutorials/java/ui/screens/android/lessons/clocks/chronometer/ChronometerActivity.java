package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.chronometer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.databinding.ActivityChronometerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

public class ChronometerActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityChronometerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChronometerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "Chronometer");
            startActivity(intent);
        });
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
        binding.buttonStart.setOnClickListener(v -> binding.chronometer.start());
        binding.buttonStop.setOnClickListener(v -> binding.chronometer.stop());
        binding.buttonReset.setOnClickListener(v -> binding.chronometer.setBase(SystemClock.elapsedRealtime()));
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
