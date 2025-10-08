package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.spinner.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentCodeBinding;
import com.d4rk.androidtutorials.java.ui.components.NoCodeAdFragment;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;

public class SpinnerTabCodeFragment extends NoCodeAdFragment<FragmentCodeBinding> {

    @Override
    @NonNull
    protected FragmentCodeBinding inflateBinding(@NonNull LayoutInflater inflater, ViewGroup container) {
        return FragmentCodeBinding.inflate(inflater, container, false);
    }

    @Override
    @NonNull
    protected View getAdView(@NonNull FragmentCodeBinding binding) {
        return binding.adView;
    }

    @Override
    protected void onBindingCreated(@NonNull FragmentCodeBinding binding, Bundle savedInstanceState) {
        CodeViewUtils.populateFromRawResource(
                binding.codeView,
                R.raw.text_spinner_java,
                CodeViewUtils.HighlightMode.JAVA,
                "SpinnerTabCode");
    }
}
