package com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.databinding.ActivityProgressBarBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ProgressBarActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityProgressBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgressBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate.apply(this, binding.container);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.progressBar.hide();
        binding.buttonDownloadHorizontal.setOnClickListener(v -> {
            int[] progressStatus = {0};
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (progressStatus[0] < 100) {
                        progressStatus[0]++;
                        binding.progressBarHorizontal.setProgress(progressStatus[0]);
                        handler.postDelayed(this, 50);
                    }
                }
            };
            handler.post(runnable);
        });
        binding.buttonDownload.setOnClickListener(v -> {
            binding.progressBar.show();
            handler.postDelayed(() -> binding.progressBar.hide(), 5000);
        });
        binding.floatingButtonShowSyntax.setOnClickListener(v ->
                startActivity(new Intent(this, ProgressBarCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
