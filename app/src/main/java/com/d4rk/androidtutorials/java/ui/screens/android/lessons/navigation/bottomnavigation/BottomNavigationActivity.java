package com.d4rk.androidtutorials.java.ui.screens.android.lessons.navigation.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.R;

import com.d4rk.androidtutorials.java.databinding.ActivityBottomNavigationBinding;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

public class BottomNavigationActivity extends UpNavigationActivity {
    private final Handler handler = new Handler();
    private ActivityBottomNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdgeBottomBar(binding.container, binding.bottomNav);

        binding.bottomNav.setOnItemSelectedListener(item -> {
            binding.textView.setText(getString(R.string.selected) + " " + item.getTitle());
            return true;
        });

        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "BottomNavigation");
            startActivity(intent);
        });
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
}
