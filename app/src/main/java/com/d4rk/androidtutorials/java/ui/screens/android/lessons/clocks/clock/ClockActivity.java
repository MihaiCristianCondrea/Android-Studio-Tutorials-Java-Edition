package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityClockBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs.ClockTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs.ClockTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.util.Arrays;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ClockActivity extends SyntaxFabActivity {
    private ActivityClockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_clocks,
                true);


        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.clocks,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        ClockTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        ClockTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }
}
