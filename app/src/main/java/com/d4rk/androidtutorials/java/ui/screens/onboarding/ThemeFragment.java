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
        binding.setFirstIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_theme));
        binding.setFirstTitle(getString(R.string.light_mode));
        binding.setFirstDescription(getString(R.string.light_mode_description));
        binding.setSecondIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_about));
        binding.setSecondTitle(getString(R.string.dark_mode));
        binding.setSecondDescription(getString(R.string.dark_mode_description));
        binding.setThirdIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_android));
        binding.setThirdTitle(getString(R.string.follow_system));
        binding.setThirdDescription(getString(R.string.follow_system_mode_description));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        // Assign unique IDs to each radio button
        binding.optionFirst.radioButton.setId(View.generateViewId());
        binding.optionSecond.radioButton.setId(View.generateViewId());
        binding.optionThird.radioButton.setId(View.generateViewId());

        // Default selection
        binding.optionThird.radioButton.setChecked(true);

        binding.cardFirst.setOnClickListener(v -> binding.optionFirst.radioButton.setChecked(true));
        binding.cardSecond.setOnClickListener(v -> binding.optionSecond.radioButton.setChecked(true));
        binding.cardThird.setOnClickListener(v -> binding.optionThird.radioButton.setChecked(true));
    }

    public void saveSelection() {
        String[] values = getResources().getStringArray(R.array.preference_theme_values);
        String value = values[0];
        if (binding.optionFirst.radioButton.isChecked()) {
            value = values[1];
        } else if (binding.optionSecond.radioButton.isChecked()) {
            value = values[2];
        }
        viewModel.setTheme(value);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

