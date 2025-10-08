package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.relative.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.FragmentLayoutBinding;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;

public class RelativeLayoutTabLayoutFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentLayoutBinding binding = FragmentLayoutBinding.inflate(inflater, container, false);
        AdUtils.loadBanner(binding.adView);
        CodeViewUtils.populateFromRawResource(
                binding.codeView,
                R.raw.text_relative_layout_xml,
                CodeViewUtils.HighlightMode.XML,
                "RelativeLayoutTabLayout");
        return binding.getRoot();
    }

}