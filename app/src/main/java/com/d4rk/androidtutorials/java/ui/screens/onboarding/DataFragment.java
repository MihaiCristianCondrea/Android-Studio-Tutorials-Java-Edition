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
import com.d4rk.androidtutorials.java.utils.ConsentUtils;
import androidx.preference.PreferenceManager;
import android.content.SharedPreferences;
import com.d4rk.androidtutorials.java.R;

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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String keyAnalytics = getString(R.string.key_consent_analytics);
        String keyAdPersonalization = getString(R.string.key_consent_ad_personalization);

        boolean analytics = prefs.getBoolean(keyAnalytics, true);
        boolean ads = prefs.getBoolean(keyAdPersonalization, true);
        binding.switchCrashlytics.setChecked(analytics);
        binding.switchAds.setChecked(ads);

        binding.switchCrashlytics.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setCrashlyticsEnabled(isChecked);
            viewModel.setConsentAnalytics(isChecked);
            ConsentUtils.updateFirebaseConsent(requireContext(), isChecked, binding.switchAds.isChecked(), binding.switchAds.isChecked(), binding.switchAds.isChecked());
        });

        binding.switchAds.setOnCheckedChangeListener((buttonView, isChecked) -> {
            viewModel.setConsentAdStorage(isChecked);
            viewModel.setConsentAdUserData(isChecked);
            viewModel.setConsentAdPersonalization(isChecked);
            ConsentUtils.updateFirebaseConsent(requireContext(), binding.switchCrashlytics.isChecked(), isChecked, isChecked, isChecked);
        });

        binding.linkPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://mihaicristiancondrea.github.io/profile/#privacy-policy-end-user-software"));
            startActivity(intent);
        });
    }

    public void saveSelection() {
        boolean analytics = binding.switchCrashlytics.isChecked();
        boolean ads = binding.switchAds.isChecked();
        viewModel.setCrashlyticsEnabled(analytics);
        viewModel.setConsentAnalytics(analytics);
        viewModel.setConsentAdStorage(ads);
        viewModel.setConsentAdUserData(ads);
        viewModel.setConsentAdPersonalization(ads);
        ConsentUtils.updateFirebaseConsent(requireContext(), analytics, ads, ads, ads);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

