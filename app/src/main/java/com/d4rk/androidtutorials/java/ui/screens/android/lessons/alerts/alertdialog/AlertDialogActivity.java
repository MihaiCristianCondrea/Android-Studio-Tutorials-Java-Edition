package com.d4rk.androidtutorials.java.ui.screens.android.lessons.alerts.alertdialog;

import android.content.Intent;
import android.os.Bundle;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityAlertDialogBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class AlertDialogActivity extends SyntaxFabActivity {
    private ActivityAlertDialogBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlertDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        MaterialAlertDialogBuilder alertDialog = createAlertDialog();
        binding.button.setOnClickListener(v -> alertDialog.show());

        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "AlertDialog");
            startActivity(intent);
        });
    }

    private MaterialAlertDialogBuilder createAlertDialog() {
        return new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.your_title)
                .setMessage(R.string.summary_alert_dialog_message)
                .setIcon(R.drawable.ic_shop)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, null);
    }
}
