package com.d4rk.androidtutorials.java.ui.screens.android.lessons.buttons.buttons.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentSameCodeBinding;
import com.d4rk.androidtutorials.java.ui.components.NoCodeAdFragment;
import com.d4rk.androidtutorials.java.utils.CodeViewUtils;

public class ButtonsTabCodeFragment extends NoCodeAdFragment<FragmentSameCodeBinding> {

    @Override
    @NonNull
    protected FragmentSameCodeBinding inflateBinding(@NonNull LayoutInflater inflater, ViewGroup container) {
        return FragmentSameCodeBinding.inflate(inflater, container, false);
    }

    @Override
    @NonNull
    protected View getAdView(@NonNull FragmentSameCodeBinding binding) {
        return binding.adView;
    }

    @Override
    protected void onBindingCreated(@NonNull FragmentSameCodeBinding binding, Bundle savedInstanceState) {
        CodeViewUtils.populateFromRawResource(
                binding.codeView,
                R.raw.text_buttons_java,
                CodeViewUtils.HighlightMode.JAVA,
                "ButtonsTabCode");
        binding.textViewWarning.setText(R.string.same_code_buttons);
    }
}