package com.d4rk.androidtutorials.java.ui.screens.help;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityHelpBinding;
import com.d4rk.androidtutorials.java.databinding.DialogVersionInfoBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.BaseActivity;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.d4rk.androidtutorials.java.utils.OpenSourceLicensesUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@AndroidEntryPoint
public class HelpActivity extends BaseActivity {

    private HelpViewModel helpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelpBinding binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeDelegate.apply(this, binding.getRoot());
        AdUtils.loadBanner(binding.faqNativeAd);
        helpViewModel = new ViewModelProvider(this).get(HelpViewModel.class);
        new FastScrollerBuilder(binding.scrollContainer)
                .useMd2Style()
                .build();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_faq, new FaqFragment())
                .commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout_feedback, new FeedbackFragment())
                .commit();
    }

    public HelpViewModel getHelpViewModel() {
        return helpViewModel;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == android.R.id.home) {
            finish();
            return true;
        } else if (itemId == R.id.view_in_google_play) {
            openGooglePlayListing();
            return true;
        } else if (itemId == R.id.version_info) {
            showVersionInfoDialog();
            return true;
        } else if (itemId == R.id.beta_program) {
            openLink("https://play.google.com/apps/testing/" + getPackageName());
            return true;
        } else if (itemId == R.id.terms_of_service) {
            openLink("https://mihaicristiancondrea.github.io/profile/#terms-of-service-end-user-software");
            return true;
        } else if (itemId == R.id.privacy_policy) {
            openLink("https://mihaicristiancondrea.github.io/profile/#privacy-policy-end-user-software");
            return true;
        } else if (itemId == R.id.oss) {
            OpenSourceLicensesUtils.openLicensesScreen(this);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showVersionInfoDialog() {
        DialogVersionInfoBinding binding = DialogVersionInfoBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());

        binding.appIcon.setImageResource(R.mipmap.ic_launcher);
        binding.appName.setText(getString(R.string.app_name));
        binding.appVersion.setText(getString(R.string.version, BuildConfig.VERSION_NAME));
        binding.appCopyright.setText(getString(R.string.copyright));

        builder.setPositiveButton(android.R.string.ok, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void openGooglePlayListing() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void openLink(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    public static class FaqFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_faq, rootKey);
        }
    }

    public static class FeedbackFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences_feedback, rootKey);

            Preference feedbackPreference = findPreference(getString(R.string.key_feedback));
            if (feedbackPreference != null) {
                feedbackPreference.setOnPreferenceClickListener(preference -> {
                    if (requireActivity() instanceof HelpActivity helpActivity) {
                        HelpViewModel vm = helpActivity.getHelpViewModel();

                        vm.requestReviewFlow(new HelpRepository.OnReviewInfoListener() {
                            @Override
                            public void onSuccess(ReviewInfo info) {
                                vm.launchReviewFlow(helpActivity, info);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                launchGooglePlayReviews();
                            }
                        });
                    }
                    return true;
                });
            }
        }

        private void launchGooglePlayReviews() {
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + requireActivity().getPackageName() + "&showAllReviews=true");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Snackbar.make(requireView(),
                                R.string.snack_unable_to_open_google_play_store,
                                Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }
}