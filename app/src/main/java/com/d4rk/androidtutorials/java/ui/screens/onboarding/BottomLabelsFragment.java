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
import com.d4rk.androidtutorials.java.databinding.FragmentOnboardingBottomLabelsBinding;

public class BottomLabelsFragment extends Fragment {

    private FragmentOnboardingBottomLabelsBinding binding;
    private OnboardingViewModel viewModel;

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
        binding.buttonNext.setOnClickListener(v -> {
            int checkedId = binding.labelsGroup.getCheckedRadioButtonId();
            String[] values = getResources().getStringArray(R.array.preference_bottom_navigation_bar_labels_values);
            String value = values[0];
            if (checkedId == R.id.radio_selected) {
                value = values[1];
            } else if (checkedId == R.id.radio_unlabeled) {
                value = values[2];
            }
            viewModel.setBottomNavLabels(value);
            ((OnboardingActivity) requireActivity()).nextPage();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

