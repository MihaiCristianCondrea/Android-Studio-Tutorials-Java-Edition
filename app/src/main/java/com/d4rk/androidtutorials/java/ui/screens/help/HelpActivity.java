package com.d4rk.androidtutorials.java.ui.screens.help;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityHelpBinding;
import com.d4rk.androidtutorials.java.databinding.DialogVersionInfoBinding;
import com.d4rk.androidtutorials.java.databinding.ItemHelpFaqBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.d4rk.androidtutorials.java.utils.OpenSourceLicensesUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;

import java.util.Arrays;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

@AndroidEntryPoint
public class HelpActivity extends UpNavigationActivity {

    private static final List<FaqItem> FAQ_ITEMS = Arrays.asList(
            new FaqItem(R.string.question_1, R.string.summary_preference_faq_1),
            new FaqItem(R.string.question_2, R.string.summary_preference_faq_2),
            new FaqItem(R.string.question_3, R.string.summary_preference_faq_3),
            new FaqItem(R.string.question_4, R.string.summary_preference_faq_4),
            new FaqItem(R.string.question_5, R.string.summary_preference_faq_5),
            new FaqItem(R.string.question_6, R.string.summary_preference_faq_6),
            new FaqItem(R.string.question_7, R.string.summary_preference_faq_7),
            new FaqItem(R.string.question_8, R.string.summary_preference_faq_8),
            new FaqItem(R.string.question_9, R.string.summary_preference_faq_9)
    );
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityHelpBinding binding;
    private HelpViewModel helpViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        AdUtils.loadBanner(binding.faqNativeAd);
        helpViewModel = new ViewModelProvider(this).get(HelpViewModel.class);
        new FastScrollerBuilder(binding.scrollView)
                .useMd2Style()
                .build();
        bindFaqItems(binding);
        setupContactSupportCard();
        setupFeedbackFab();
        handler.postDelayed(() -> binding.fabContactSupport.shrink(), 5000L);
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

    private void setupContactSupportCard() {
        binding.contactSupportCard.setOnClickListener(v -> openSupportEmail());
    }

    private void setupFeedbackFab() {
        binding.fabContactSupport.setOnClickListener(v -> requestReview());
        binding.fabContactSupport.setContentDescription(getString(R.string.send_feedback));
    }

    private void openSupportEmail() {
        String supportEmail = getString(R.string.contact_support_email);
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.fromParts("mailto", supportEmail, null));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{supportEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT,
                getString(R.string.contact_support_email_subject, getString(R.string.app_name)));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.contact_support_email_body));

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.contact_support_title)));
        } else {
            Snackbar.make(binding.getRoot(), R.string.support_link_unavailable, Snackbar.LENGTH_SHORT).show();
        }
    }

    private void requestReview() {
        binding.fabContactSupport.setEnabled(false);
        helpViewModel.requestReviewFlow(new HelpRepository.OnReviewInfoListener() {
            @Override
            public void onSuccess(ReviewInfo info) {
                helpViewModel.launchReviewFlow(HelpActivity.this, info);
                binding.fabContactSupport.setEnabled(true);
            }

            @Override
            public void onFailure(Exception e) {
                binding.fabContactSupport.setEnabled(true);
                launchGooglePlayReviews();
            }
        });
    }

    private void launchGooglePlayReviews() {
        Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName() + "&showAllReviews=true");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Snackbar.make(binding.getRoot(),
                            R.string.snack_unable_to_open_google_play_store,
                            Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void bindFaqItems(ActivityHelpBinding binding) {
        LinearLayoutCompat faqList = binding.faqList;
        faqList.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < FAQ_ITEMS.size(); i++) {
            FaqItem item = FAQ_ITEMS.get(i);
            ItemHelpFaqBinding itemBinding = ItemHelpFaqBinding.inflate(inflater, faqList, false);
            itemBinding.question.setText(item.questionResId);
            itemBinding.answer.setText(item.answerResId);
            itemBinding.answer.setVisibility(View.GONE);
            itemBinding.toggleIcon.setRotation(0f);
            CharSequence questionText = itemBinding.question.getText();
            itemBinding.getRoot().setContentDescription(questionText);
            itemBinding.questionContainer.setContentDescription(questionText);
            ViewCompat.setStateDescription(itemBinding.getRoot(), getString(R.string.faq_state_collapsed));
            ViewCompat.setStateDescription(itemBinding.questionContainer, getString(R.string.faq_state_collapsed));

            View.OnClickListener toggleListener = v -> toggleFaqItem(itemBinding);
            itemBinding.getRoot().setOnClickListener(toggleListener);
            itemBinding.questionContainer.setOnClickListener(toggleListener);
            itemBinding.toggleIcon.setOnClickListener(toggleListener);
            itemBinding.divider.setVisibility(i == FAQ_ITEMS.size() - 1 ? View.GONE : View.VISIBLE);
            faqList.addView(itemBinding.getRoot());
        }
    }

    private void toggleFaqItem(ItemHelpFaqBinding binding) {
        boolean expand = binding.answer.getVisibility() != View.VISIBLE;
        binding.answer.setVisibility(expand ? View.VISIBLE : View.GONE);
        float rotation = expand ? 180f : 0f;
        binding.toggleIcon.animate().cancel();
        binding.toggleIcon.animate()
                .rotation(rotation)
                .setDuration(200L)
                .start();
        int stateRes = expand ? R.string.faq_state_expanded : R.string.faq_state_collapsed;
        CharSequence stateDescription = getString(stateRes);
        ViewCompat.setStateDescription(binding.getRoot(), stateDescription);
        ViewCompat.setStateDescription(binding.questionContainer, stateDescription);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private record FaqItem(@StringRes int questionResId, @StringRes int answerResId) {
    }
}
