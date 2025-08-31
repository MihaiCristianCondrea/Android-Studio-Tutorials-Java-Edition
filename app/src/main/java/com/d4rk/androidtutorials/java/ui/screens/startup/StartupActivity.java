package com.d4rk.androidtutorials.java.ui.screens.startup;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityStartupBinding;
import com.d4rk.androidtutorials.java.ui.screens.main.MainActivity;
import com.d4rk.androidtutorials.java.ui.screens.startup.dialogs.ConsentDialogFragment;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;
import com.d4rk.androidtutorials.java.utils.ConsentUtils;


import me.zhanghai.android.fastscroll.FastScrollerBuilder;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StartupActivity extends AppCompatActivity {

    private StartupViewModel startupViewModel;
    private ConsentInformation consentInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.d4rk.androidtutorials.java.databinding.ActivityStartupBinding binding = ActivityStartupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ConsentUtils.applyStoredConsent(this);

        startupViewModel = new ViewModelProvider(this).get(StartupViewModel.class);

        consentInformation = UserMessagingPlatform.getConsentInformation(this);
        ConsentRequestParameters params = new ConsentRequestParameters.Builder()
                .setTagForUnderAgeOfConsent(false)
                .build();

        startupViewModel.requestConsentInfoUpdate(
                this,
                params,
                () -> {
                    if (consentInformation.isConsentFormAvailable()) {
                        startupViewModel.loadConsentForm(
                                this,
                                formError -> ConsentUtils.updateFirebaseConsent(this,
                                        false, false, false, false)
                        );
                    } else if (consentInformation.getConsentStatus() == ConsentInformation.ConsentStatus.OBTAINED) {
                        ConsentUtils.applyStoredConsent(this);
                    }
                },
                formError -> {}
        );

        new FastScrollerBuilder(binding.scrollView)
                .useMd2Style()
                .build();

        binding.buttonBrowsePrivacyPolicyAndTermsOfService.setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://d4rk7355608.github.io/profile/#privacy-policy-apps")))
        );

        binding.floatingButtonAgree.setOnClickListener(v -> {
            ConsentDialogFragment dialog = new ConsentDialogFragment();
            dialog.setConsentListener((analytics, adStorage, adUserData, adPersonalization) -> {
                ConsentUtils.updateFirebaseConsent(this,
                        analytics, adStorage, adUserData, adPersonalization);
                proceedToMainActivity();
            });
            dialog.show(getSupportFragmentManager(), "consent_dialog");
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
        }
    }

    private void proceedToMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}