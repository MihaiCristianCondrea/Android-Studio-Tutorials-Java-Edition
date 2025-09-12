package com.d4rk.androidtutorials.java.ui.screens.startup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.databinding.ActivityStartupBinding;
import com.d4rk.androidtutorials.java.ui.screens.onboarding.OnboardingActivity;
import com.google.android.ump.ConsentRequestParameters;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@AndroidEntryPoint
public class StartupActivity extends AppCompatActivity {

    private StartupViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                        Uri.parse("https://mihaicristiancondrea.github.io/profile/#privacy-policy-end-user-software")))
        );

        binding.floatingButtonAgree.setOnClickListener(v -> {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        });
    }
}