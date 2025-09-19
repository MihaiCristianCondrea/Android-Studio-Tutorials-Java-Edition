package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.history;

import android.os.Bundle;

import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityAndroidHistoryBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class AndroidHistory extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAndroidHistoryBinding binding = ActivityAndroidHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowCompat.enableEdgeToEdge(this.getWindow());

        AdUtils.loadBanner(binding.adView);
        AdUtils.loadBanner(binding.adViewBottom);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
    }
}