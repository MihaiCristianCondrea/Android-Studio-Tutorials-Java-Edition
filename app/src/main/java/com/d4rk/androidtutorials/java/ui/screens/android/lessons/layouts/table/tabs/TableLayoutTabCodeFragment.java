package com.d4rk.androidtutorials.java.ui.screens.android.lessons.layouts.table.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.d4rk.androidtutorials.java.databinding.FragmentNoCodeBinding;
import com.d4rk.androidtutorials.java.ads.AdUtils;

public class TableLayoutTabCodeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.d4rk.androidtutorials.java.databinding.FragmentNoCodeBinding binding = FragmentNoCodeBinding.inflate(inflater, container, false);
        AdUtils.loadBanner(binding.adView);
        return binding.getRoot();
    }
}