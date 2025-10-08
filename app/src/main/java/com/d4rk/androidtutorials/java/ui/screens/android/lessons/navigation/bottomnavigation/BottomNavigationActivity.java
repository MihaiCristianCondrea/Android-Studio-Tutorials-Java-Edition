package com.d4rk.androidtutorials.java.ui.screens.android.lessons.navigation.bottomnavigation;

import android.content.Intent;
import android.os.Bundle;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityBottomNavigationBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class BottomNavigationActivity extends SyntaxFabActivity {
    private ActivityBottomNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBottomNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        binding.bottomNav.setOnItemSelectedListener(item -> {
            binding.textView.setText(getString(R.string.selected_format, item.getTitle()));
            return true;
        });

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "BottomNavigation");
            startActivity(intent);
        });
    }
}
