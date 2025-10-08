package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.spinner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivitySpinnerBinding;
import com.d4rk.androidtutorials.java.databinding.ActivitySpinnerContentBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class SpinnerActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivitySpinnerBinding binding;
    private ActivitySpinnerContentBinding contentBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        contentBinding = ActivitySpinnerContentBinding.inflate(
                getLayoutInflater(),
                binding.lessonScaffold.lessonContentContainer,
                true
        );

        contentBinding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        contentBinding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        contentBinding.descriptionSection.descriptionText.setText(R.string.summary_spinner);
        AdUtils.loadBanner(contentBinding.descriptionSection.adView);

        new FastScrollerBuilder(binding.lessonScaffold.lessonScrollView).useMd2Style().build();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.spinner_lessons_entries,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contentBinding.spinnerOptions.setAdapter(adapter);
        if (adapter.getCount() > 0) {
            CharSequence firstItem = adapter.getItem(0);
            if (firstItem != null) {
                contentBinding.textSelectedItem.setText(
                        getString(R.string.spinner_selected_format, firstItem)
                );
            }
        }
        contentBinding.spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CharSequence item = (CharSequence) parent.getItemAtPosition(position);
                contentBinding.textSelectedItem.setText(
                        getString(R.string.spinner_selected_format, item)
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                contentBinding.textSelectedItem.setText(R.string.spinner_selected_none);
            }
        });

        binding.lessonScaffold.lessonShowCodeFab.setOnClickListener(v ->
                startActivity(new Intent(this, SpinnerCodeActivity.class)));
        handler.postDelayed(() -> binding.lessonScaffold.lessonShowCodeFab.shrink(), 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
