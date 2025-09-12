package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.permissions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.d4rk.androidtutorials.java.databinding.ActivityPermissionsTutorialBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.gms.ads.MobileAds;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class PermissionsTutorialActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.d4rk.androidtutorials.java.databinding.ActivityPermissionsTutorialBinding binding = ActivityPermissionsTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        MobileAds.initialize(this);

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.scrollView);

        binding.adViewBottom.loadAd();
        binding.adViewLarge.loadAd();
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.buttonMore.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/guide/topics/permissions/overview"))));
    }
}