package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.radiobutton.MaterialRadioButton;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentOnboardingBottomLabelsBinding;

public class BottomLabelsFragment extends Fragment {

    private FragmentOnboardingBottomLabelsBinding binding;
    private OnboardingViewModel viewModel;
    private MaterialRadioButton[] radioButtons;
    private View[] optionCards;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBottomLabelsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        radioButtons = new MaterialRadioButton[]{
                binding.optionLabeled.radioButton,
                binding.optionSelected.radioButton,
                binding.optionUnlabeled.radioButton
        };

        optionCards = new View[]{
                binding.cardLabeled,
                binding.cardSelected,
                binding.cardUnlabeled
        };

        for (int i = 0; i < radioButtons.length; i++) {
            int index = i;
            radioButtons[i].setId(View.generateViewId());
            optionCards[i].setOnClickListener(v -> selectOption(index));
            radioButtons[i].setOnClickListener(v -> selectOption(index));
        }

        String current = viewModel.getBottomNavLabels();
        String[] values = getResources().getStringArray(R.array.preference_bottom_navigation_bar_labels_values);
        int index = 0;
        if (current.equals(values[1])) {
            index = 1;
        } else if (current.equals(values[2])) {
            index = 2;
        }
        selectOption(index);
    }

    private void selectOption(int index) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setChecked(i == index);
        }
    }

    public void saveSelection() {
        String[] values = getResources().getStringArray(R.array.preference_bottom_navigation_bar_labels_values);
        int index = 0;
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                index = i;
                break;
            }
        }
        viewModel.setBottomNavLabels(values[index]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

