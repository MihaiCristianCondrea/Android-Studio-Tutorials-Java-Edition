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

public class StartPageFragment extends Fragment {

    private FragmentOnboardingSelectionBinding binding;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingSelectionBinding.inflate(inflater, container, false);

        binding.setTitle(getString(R.string.default_tab));
        binding.setDescription(getString(R.string.default_tab_description));
        binding.setFirstIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_home));
        binding.setFirstTitle(getString(R.string.home));
        binding.setFirstDescription(getString(R.string.home_description));
        binding.setSecondIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_android_sdk));
        binding.setSecondTitle(getString(R.string.android_studio));
        binding.setSecondDescription(getString(R.string.android_studio_description));
        binding.setThirdIcon(ContextCompat.getDrawable(requireContext(), R.drawable.ic_about));
        binding.setThirdTitle(getString(R.string.about));
        binding.setThirdDescription(getString(R.string.about_description));

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        binding.optionFirst.radioButton.setId(View.generateViewId());
        binding.optionSecond.radioButton.setId(View.generateViewId());
        binding.optionThird.radioButton.setId(View.generateViewId());

        String[] values = getResources().getStringArray(R.array.preference_default_tab_values);
        String savedValue = viewModel.getDefaultTab();
        int selectedIndex = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(savedValue)) {
                selectedIndex = i;
                break;
            }
        }

        selectOption(selectedIndex);

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

        if (viewModel != null) {
            String[] values = getResources().getStringArray(R.array.preference_default_tab_values);
            if (index >= 0 && index < values.length) {
                viewModel.setDefaultTab(values[index]);
            }
        }
    }

    public void saveSelection() {
        if (binding.optionSecond.radioButton.isChecked()) {
            selectOption(1);
        } else if (binding.optionThird.radioButton.isChecked()) {
            selectOption(2);
        } else {
            selectOption(0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

