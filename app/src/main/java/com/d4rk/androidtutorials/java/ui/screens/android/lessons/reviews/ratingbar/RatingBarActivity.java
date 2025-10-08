package com.d4rk.androidtutorials.java.ui.screens.android.lessons.reviews.ratingbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRatingBarBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class RatingBarActivity extends SyntaxFabActivity {
    private ActivityRatingBarBinding binding;
    private float rating = 0f;
    private String formattedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        formattedString = String.format(getString(R.string.stars), rating);
        binding.textViewRatingValue.setText(formattedString);
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = rating;
            updateRatingText();
        });
        binding.button.setOnClickListener(v -> showRatingToast());
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "RatingBar");
            startActivity(intent);
        });
    }

    private void updateRatingText() {
        formattedString = String.format(getString(R.string.stars), rating);
        binding.textViewRatingValue.setText(formattedString);
    }

    private void showRatingToast() {
        formattedString = String.format(getString(R.string.snack_rating), rating);
        Toast.makeText(this, formattedString, Toast.LENGTH_SHORT).show();
    }
}
