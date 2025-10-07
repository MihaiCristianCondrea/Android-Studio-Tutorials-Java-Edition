package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs;

import android.os.Bundle;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityShortcutsDebuggingBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class DebuggingShortcutsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.d4rk.androidtutorials.java.databinding.ActivityShortcutsDebuggingBinding binding = ActivityShortcutsDebuggingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        EdgeToEdgeDelegate.apply(this, binding.scrollView);
        AdUtils.loadBanner(binding.adView);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
    }
}