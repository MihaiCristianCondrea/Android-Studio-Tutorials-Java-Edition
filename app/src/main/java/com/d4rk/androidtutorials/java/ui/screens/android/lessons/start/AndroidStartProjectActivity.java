package com.d4rk.androidtutorials.java.ui.screens.android.lessons.start;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityAndroidStartProjectBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class AndroidStartProjectActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAndroidStartProjectBinding binding = ActivityAndroidStartProjectBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        setSupportActionBar(binding.topAppBar);
        binding.topAppBar.setNavigationOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        binding.topAppBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_share) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareLink = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareLink);
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject));
                startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
                return true;
            }
            return false;
        });
        AdUtils.loadBanner(binding.adViewBottom);
        AdUtils.loadBanner(binding.adView);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.textViewThirdStepSummary.setMovementMethod(LinkMovementMethod.getInstance());
    }
}