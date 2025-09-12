package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.images;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.databinding.ActivityImagesBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ImagesActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityImagesBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(new Intent(ImagesActivity.this, ImagesCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
