package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.history;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.databinding.ActivityAndroidHistoryBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.d4rk.androidtutorials.java.ads.AdUtils;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class AndroidHistory extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAndroidHistoryBinding binding = ActivityAndroidHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeDelegate.apply(this, binding.scrollView);

        AdUtils.loadBanner(binding.adView);
        AdUtils.loadBanner(binding.adViewBottom);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
    }
}