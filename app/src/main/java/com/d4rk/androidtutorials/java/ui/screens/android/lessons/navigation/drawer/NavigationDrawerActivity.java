package com.d4rk.androidtutorials.java.ui.screens.android.lessons.navigation.drawer;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.GravityCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityNavigationDrawerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class NavigationDrawerActivity extends SyntaxFabActivity {
    private ActivityNavigationDrawerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.navigationView.setNavigationItemSelectedListener(item -> {
            binding.textView.setText(getString(R.string.selected_format, item.getTitle()));
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
        binding.buttonOpenDrawer.setOnClickListener(v -> binding.drawerLayout.openDrawer(GravityCompat.START));

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "NavigationDrawer");
            startActivity(intent);
        });
    }
}
