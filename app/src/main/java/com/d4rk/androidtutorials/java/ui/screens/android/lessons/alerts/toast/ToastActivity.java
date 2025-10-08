package com.d4rk.androidtutorials.java.ui.screens.android.lessons.alerts.toast;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityToastBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class ToastActivity extends SyntaxFabActivity {
    private ActivityToastBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityToastBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        binding.button.setOnClickListener(v ->
                Toast.makeText(this, R.string.toast_this_is_a_toast, Toast.LENGTH_SHORT).show()
        );

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "Toast");
            startActivity(intent);
        });
    }
}
