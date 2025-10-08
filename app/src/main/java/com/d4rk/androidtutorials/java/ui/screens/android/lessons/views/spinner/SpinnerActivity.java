package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.spinner;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivitySpinnerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.spinner.tabs.SpinnerTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.spinner.tabs.SpinnerTabLayoutFragment;
import com.d4rk.androidtutorials.java.ui.utils.LessonUiUtils;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.util.Arrays;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class SpinnerActivity extends SyntaxFabActivity {
    private ActivitySpinnerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        LessonUiUtils.setupDescriptionSection(binding.descriptionSection,
                R.string.summary_spinner,
                true);

        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_lessons_entries,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerOptions.setAdapter(adapter);
        if (adapter.getCount() > 0) {
            CharSequence firstItem = adapter.getItem(0);
            if (firstItem != null) {
                binding.textSelectedItem.setText(getString(R.string.spinner_selected_format, firstItem));
            }
        }
        binding.spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence item = (CharSequence) parent.getItemAtPosition(position);
                binding.textSelectedItem.setText(getString(R.string.spinner_selected_format, item));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                binding.textSelectedItem.setText(R.string.spinner_selected_none);
            }
        });

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.spinner,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        SpinnerTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        SpinnerTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
    }
}
