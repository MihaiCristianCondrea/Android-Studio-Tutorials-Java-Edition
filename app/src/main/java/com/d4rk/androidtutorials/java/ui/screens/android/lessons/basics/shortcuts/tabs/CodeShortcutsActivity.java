package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs;

import android.os.Bundle;

import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityShortcutsCodeBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class CodeShortcutsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShortcutsCodeBinding binding = ActivityShortcutsCodeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        WindowCompat.enableEdgeToEdge(this.getWindow());

        AdUtils.loadBanner(binding.adView);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
    }
}