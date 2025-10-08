package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityLinearLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear.tabs.LinearLayoutTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.linear.tabs.LinearLayoutTabLayoutFragment;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import java.util.Arrays;

public class LinearLayoutActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityLinearLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLinearLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_linear_layout_compat);
        AdUtils.loadBanner(binding.descriptionSection.adView);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.linear_layout,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        LinearLayoutTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        LinearLayoutTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
