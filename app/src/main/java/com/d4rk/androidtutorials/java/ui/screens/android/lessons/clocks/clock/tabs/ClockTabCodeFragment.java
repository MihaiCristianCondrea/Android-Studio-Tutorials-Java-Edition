package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.clock.tabs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.d4rk.androidtutorials.java.databinding.FragmentNoCodeBinding;
import com.d4rk.androidtutorials.java.ui.components.NoCodeAdFragment;

public class ClockTabCodeFragment extends NoCodeAdFragment<FragmentNoCodeBinding> {
    @Override
    @NonNull
    protected FragmentNoCodeBinding inflateBinding(@NonNull LayoutInflater inflater, ViewGroup container) {
        return FragmentNoCodeBinding.inflate(inflater, container, false);
    }

    @Override
    @NonNull
    protected View getAdView(@NonNull FragmentNoCodeBinding binding) {
        return binding.adView;
    }
}