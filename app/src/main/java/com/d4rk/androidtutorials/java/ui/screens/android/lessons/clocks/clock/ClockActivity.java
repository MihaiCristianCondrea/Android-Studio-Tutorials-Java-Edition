package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock;

import android.os.Bundle;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityClockBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs.ClockTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs.ClockTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;
import java.util.Arrays;

public class ClockActivity extends SyntaxFabActivity {
    private ActivityClockBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_clocks);
        AdUtils.loadBanner(binding.descriptionSection.adView);


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
