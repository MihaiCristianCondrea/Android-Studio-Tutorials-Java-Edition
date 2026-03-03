package com.d4rk.androidtutorials.java.ui.screens.android.lessons.architecture.viewmodel;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityViewModelBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.architecture.viewmodel.tabs.ViewModelTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.architecture.viewmodel.tabs.ViewModelTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.util.Arrays;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ViewModelActivity extends SyntaxFabActivity {

    private ActivityViewModelBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewModelBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_view_model,
                true);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        CounterViewModel counterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        binding.incrementButton.setOnClickListener(view -> counterViewModel.incrementCounter());
        counterViewModel.getCounter().observe(this, value ->
                binding.counterValue.setText(getString(R.string.view_model_counter_value, value)));

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.view_model,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        ViewModelTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        ViewModelTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }
}
