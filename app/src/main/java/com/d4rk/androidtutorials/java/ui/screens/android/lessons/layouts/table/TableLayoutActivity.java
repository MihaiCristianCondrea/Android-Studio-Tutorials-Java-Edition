package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.table;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityTableLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.table.tabs.TableLayoutTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.table.tabs.TableLayoutTabLayoutFragment;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import java.util.Arrays;

public class TableLayoutActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityTableLayoutBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTableLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.table_layout,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        TableLayoutTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        TableLayoutTabLayoutFragment.class,
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
