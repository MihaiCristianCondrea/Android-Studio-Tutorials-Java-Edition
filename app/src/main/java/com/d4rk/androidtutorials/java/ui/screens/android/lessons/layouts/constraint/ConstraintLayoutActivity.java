package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.constraint;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityConstraintLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.constraint.tabs.ConstraintLayoutTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.constraint.tabs.ConstraintLayoutTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.util.Arrays;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ConstraintLayoutActivity extends SyntaxFabActivity {

    private ActivityConstraintLayoutBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConstraintLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_constraint_layout,
                true);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.constraint_layout,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        ConstraintLayoutTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        ConstraintLayoutTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }
}
