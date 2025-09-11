package com.d4rk.androidtutorials.java.ui.screens.startup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.databinding.ActivityStartupBinding;
import com.d4rk.androidtutorials.java.ui.screens.main.MainActivity;
import com.d4rk.androidtutorials.java.ui.screens.onboarding.OnboardingActivity;
import com.d4rk.androidtutorials.java.data.repository.OnboardingRepository;
import com.google.android.ump.ConsentRequestParameters;
import com.d4rk.androidtutorials.java.ui.screens.startup.StartupViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@AndroidEntryPoint
public class StartupActivity extends AppCompatActivity {

    @Inject
    OnboardingRepository onboardingRepository;

    private StartupViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (onboardingRepository.isOnboardingComplete()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        ActivityStartupBinding binding = ActivityStartupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(StartupViewModel.class);
        ConsentRequestParameters params = new ConsentRequestParameters.Builder().build();
        viewModel.requestConsentInfoUpdate(this, params,
                () -> viewModel.loadConsentForm(this, null), null);

        new FastScrollerBuilder(binding.scrollView)
                .useMd2Style()
                .build();

        binding.buttonBrowsePrivacyPolicyAndTermsOfService.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://d4rk7355608.github.io/profile/#privacy-policy-apps")))
        );

        binding.floatingButtonAgree.setOnClickListener(v -> {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        });
    }
}