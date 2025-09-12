package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentOnboardingFontBinding;

public class FontFragment extends Fragment {

    private FragmentOnboardingFontBinding binding;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingFontBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);
    }

    public void saveSelection() {
        int checkedId = binding.fontGroup.getCheckedRadioButtonId();
        String[] values = getResources().getStringArray(R.array.code_font_values);
        String value = values[6];
        if (checkedId == R.id.radio_font_audiowide) {
            value = values[0];
        } else if (checkedId == R.id.radio_font_fira_code) {
            value = values[1];
        } else if (checkedId == R.id.radio_font_jetbrains_mono) {
            value = values[2];
        } else if (checkedId == R.id.radio_font_noto_sans_mono) {
            value = values[3];
        } else if (checkedId == R.id.radio_font_poppins) {
            value = values[4];
        } else if (checkedId == R.id.radio_font_roboto_mono) {
            value = values[5];
        }
        viewModel.setMonospaceFont(value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
