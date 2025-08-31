package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import com.d4rk.androidtutorials.java.databinding.ActivityClockBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ClockActivity extends UpNavigationActivity {
    private final Handler handler = new Handler();
    private ActivityClockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);


        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.floatingButtonShowSyntax.setOnClickListener(view -> startActivity(new Intent(this, ClockCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
}