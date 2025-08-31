package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import com.d4rk.androidtutorials.java.databinding.ActivityShortcutsGeneralBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class GeneralShortcutsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.d4rk.androidtutorials.java.databinding.ActivityShortcutsGeneralBinding binding = ActivityShortcutsGeneralBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.scrollView);

        binding.adView.loadAd(new AdRequest.Builder().build());
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
    }
}