package com.d4rk.androidtutorials.java.ui.screens.android.lessons.lists.recyclerview;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRecyclerViewBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.lists.recyclerview.tabs.RecyclerViewTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.lists.recyclerview.tabs.RecyclerViewTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.util.Arrays;
import java.util.List;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class RecyclerViewActivity extends SyntaxFabActivity {

    private ActivityRecyclerViewBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_recycler_view,
                true);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        binding.recyclerViewPreview.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewPreview.setAdapter(new RecyclerViewPreviewAdapter(getLessonTitles()));

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.recycler_view,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        RecyclerViewTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        RecyclerViewTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }

    private List<String> getLessonTitles() {
        return Arrays.asList(
                getString(R.string.linear_layout),
                getString(R.string.constraint_layout),
                getString(R.string.recycler_view),
                getString(R.string.view_model)
        );
    }
}
