package com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityProgressBarBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar.tabs.ProgressBarTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar.tabs.ProgressBarTabLayoutFragment;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import java.util.Arrays;

public class ProgressBarActivity extends SyntaxFabActivity {
    private final Handler progressHandler = new Handler(Looper.getMainLooper());
    private ActivityProgressBarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProgressBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_progress_bar);
        AdUtils.loadBanner(binding.descriptionSection.adView);

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
                        progressHandler.postDelayed(this, 50);
                    }
                }
            };
            progressHandler.post(runnable);
        });
        binding.buttonDownload.setOnClickListener(v -> {
            binding.progressBar.show();
            progressHandler.postDelayed(() -> binding.progressBar.hide(), 5000);
        });
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.progress_bar,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        ProgressBarTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        ProgressBarTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressHandler.removeCallbacksAndMessages(null);
    }
}
