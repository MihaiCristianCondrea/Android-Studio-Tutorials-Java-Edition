package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.images.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.d4rk.androidtutorials.java.databinding.FragmentNoCodeBinding;
import com.google.android.gms.ads.MobileAds;

public class ImagesTabCodeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        com.d4rk.androidtutorials.java.databinding.FragmentNoCodeBinding binding = FragmentNoCodeBinding.inflate(inflater, container, false);
        MobileAds.initialize(requireContext());
        binding.adView.loadAd();
        return binding.getRoot();
    }
}