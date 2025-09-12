package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatDelegate;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentOnboardingSelectionBinding;

public class ThemeFragment extends Fragment {

    private FragmentOnboardingSelectionBinding binding;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingSelectionBinding.inflate(inflater, container, false);

        binding.setTitle(getString(R.string.choose_your_style));
        binding.setDescription(getString(R.string.select_how_you_d_like_the_app_to_look));
        binding.setFirstIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_light_mode));
        binding.setFirstTitle(getString(R.string.light_mode));
        binding.setFirstDescription(getString(R.string.light_mode_description));
        binding.setSecondIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_dark_mode));
        binding.setSecondTitle(getString(R.string.dark_mode));
        binding.setSecondDescription(getString(R.string.dark_mode_description));
        binding.setThirdIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_system_mode));
        binding.setThirdTitle(getString(R.string.follow_system));
        binding.setThirdDescription(getString(R.string.follow_system_mode_description));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        binding.optionFirst.radioButton.setId(View.generateViewId());
        binding.optionSecond.radioButton.setId(View.generateViewId());
        binding.optionThird.radioButton.setId(View.generateViewId());

        selectOption(2);

        binding.cardFirst.setOnClickListener(v -> selectOption(0));
        binding.cardSecond.setOnClickListener(v -> selectOption(1));
        binding.cardThird.setOnClickListener(v -> selectOption(2));

        binding.optionFirst.radioButton.setOnClickListener(v -> selectOption(0));
        binding.optionSecond.radioButton.setOnClickListener(v -> selectOption(1));
        binding.optionThird.radioButton.setOnClickListener(v -> selectOption(2));
    }

    private void selectOption(int index) {
        binding.optionFirst.radioButton.setChecked(index == 0);
        binding.optionSecond.radioButton.setChecked(index == 1);
        binding.optionThird.radioButton.setChecked(index == 2);

        int mode;
        if (index == 0) {
            mode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if (index == 1) {
            mode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    public void saveSelection() {
        String[] values = getResources().getStringArray(R.array.preference_theme_values);
        String value;
        if (binding.optionFirst.radioButton.isChecked()) {
            value = values[1];
        } else if (binding.optionSecond.radioButton.isChecked()) {
            value = values[2];
        } else {
            value = values[0];
        }
        viewModel.setTheme(value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

