package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.databinding.ActivityLinearLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class LinearLayoutActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityLinearLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinearLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(new Intent(LinearLayoutActivity.this, LinearLayoutCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
