package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.history;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import com.d4rk.androidtutorials.java.databinding.ActivityAndroidHistoryBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class AndroidHistory extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAndroidHistoryBinding binding = ActivityAndroidHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.scrollView);

        binding.adView.loadAd(new AdRequest.Builder().build());
        binding.adViewBottom.loadAd(new AdRequest.Builder().build());
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
    }
}