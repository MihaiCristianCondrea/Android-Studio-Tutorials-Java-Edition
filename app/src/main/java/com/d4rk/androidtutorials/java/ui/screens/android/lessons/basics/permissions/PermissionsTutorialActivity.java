package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.permissions;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.view.WindowCompat;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityPermissionsTutorialBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class PermissionsTutorialActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.d4rk.androidtutorials.java.databinding.ActivityPermissionsTutorialBinding binding = ActivityPermissionsTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        AdUtils.loadBanner(binding.adViewBottom);
        AdUtils.loadBanner(binding.adViewLarge);
        new FastScrollerBuilder(binding.scrollView).useMd2Style().build();
        binding.buttonMore.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com/guide/topics/permissions/overview"))));
    }
}