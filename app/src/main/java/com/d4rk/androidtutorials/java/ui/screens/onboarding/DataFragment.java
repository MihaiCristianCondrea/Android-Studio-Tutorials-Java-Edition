package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.databinding.FragmentOnboardingDataBinding;

public class DataFragment extends Fragment {

    private FragmentOnboardingDataBinding binding;
    private OnboardingViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingDataBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        binding.switchCrashlytics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.textDetails.setVisibility(isChecked ? View.VISIBLE : View.GONE);
            viewModel.setCrashlyticsEnabled(isChecked);
        });

        binding.linkPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://d4rk7355608.github.io/profile/#privacy-policy-apps"));
            startActivity(intent);
        });
    }

    public void saveSelection() {
        viewModel.setCrashlyticsEnabled(binding.switchCrashlytics.isChecked());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

