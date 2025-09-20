package com.d4rk.androidtutorials.java.ui.screens.help;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityHelpBinding;
import com.d4rk.androidtutorials.java.databinding.DialogVersionInfoBinding;
import com.d4rk.androidtutorials.java.databinding.ItemHelpFaqBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.BaseActivity;
import com.d4rk.androidtutorials.java.ui.screens.help.repository.HelpRepository;
import com.d4rk.androidtutorials.java.utils.OpenSourceLicensesUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.review.ReviewInfo;

import dagger.hilt.android.AndroidEntryPoint;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

import java.util.Arrays;
import java.util.List;

@AndroidEntryPoint
public class HelpActivity extends BaseActivity {

    private HelpViewModel helpViewModel;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHelpBinding binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AdUtils.loadBanner(binding.faqNativeAd);
        helpViewModel = new ViewModelProvider(this).get(HelpViewModel.class);
        new FastScrollerBuilder(binding.scrollView)
                .useMd2Style()
                .build();
        bindFaqItems(binding);

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

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            RecyclerView listView = getListView();
            listView.setNestedScrollingEnabled(false);
            listView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            listView.setClipToPadding(false);

            ViewGroup.LayoutParams layoutParams = listView.getLayoutParams();
            FrameLayout.LayoutParams frameLayoutParams;
            if (layoutParams instanceof FrameLayout.LayoutParams) {
                frameLayoutParams = (FrameLayout.LayoutParams) layoutParams;
            } else {
                frameLayoutParams = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
            }
            frameLayoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            frameLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            listView.setLayoutParams(frameLayoutParams);
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
            itemBinding.questionContainer.setContentDescription(itemBinding.question.getText());
            ViewCompat.setStateDescription(itemBinding.questionContainer, getString(R.string.faq_state_collapsed));
            itemBinding.questionContainer.setOnClickListener(v -> toggleFaqItem(itemBinding));
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
        ViewCompat.setStateDescription(binding.questionContainer,
                getString(expand ? R.string.faq_state_expanded : R.string.faq_state_collapsed));
    }

    private static final class FaqItem {
        @StringRes
        private final int questionResId;
        @StringRes
        private final int answerResId;

        private FaqItem(@StringRes int questionResId, @StringRes int answerResId) {
            this.questionResId = questionResId;
            this.answerResId = answerResId;
        }
    }
}