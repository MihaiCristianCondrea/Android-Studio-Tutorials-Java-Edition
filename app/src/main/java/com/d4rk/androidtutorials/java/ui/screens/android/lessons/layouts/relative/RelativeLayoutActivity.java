package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.relative;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRelativeLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.relative.tabs.RelativeLayoutTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.relative.tabs.RelativeLayoutTabLayoutFragment;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import java.util.Arrays;

public class RelativeLayoutActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityRelativeLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRelativeLayoutBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.relative_layout,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        RelativeLayoutTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        RelativeLayoutTabLayoutFragment.class,
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
