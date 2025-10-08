package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.spinner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivitySpinnerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class SpinnerActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivitySpinnerBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySpinnerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.descriptionSection.descriptionHeader.getRoot().setText(R.string.description);
        binding.layoutPreviewHeader.getRoot().setText(R.string.layout_preview);
        binding.descriptionSection.descriptionText.setText(R.string.summary_spinner);
        AdUtils.loadBanner(binding.descriptionSection.adView);

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

        binding.floatingButtonShowSyntax.setOnClickListener(v ->
                startActivity(new Intent(this, SpinnerCodeActivity.class)));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
