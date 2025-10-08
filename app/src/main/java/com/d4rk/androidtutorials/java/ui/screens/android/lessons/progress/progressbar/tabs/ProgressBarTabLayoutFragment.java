package com.d4rk.androidtutorials.java.ui.screens.android.lessons.progress.progressbar.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentLinearLayoutLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;

public class ProgressBarTabLayoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLinearLayoutLayoutBinding binding = FragmentLinearLayoutLayoutBinding.inflate(inflater, container, false);
        AdUtils.loadBanner(binding.adView);
        CodeViewUtils.populateFromRawResource(
                binding.codeViewVerticalXml,
                R.raw.text_progress_bar_xml,
                CodeViewUtils.HighlightMode.XML,
                "ProgressBarTabLayout");
        CodeViewUtils.populateFromRawResource(
                binding.codeViewHorizontalXml,
                R.raw.text_linear_layout_horizontal_xml,
                CodeViewUtils.HighlightMode.XML,
                "ProgressBarTabLayout");
        return binding.getRoot();
    }

}