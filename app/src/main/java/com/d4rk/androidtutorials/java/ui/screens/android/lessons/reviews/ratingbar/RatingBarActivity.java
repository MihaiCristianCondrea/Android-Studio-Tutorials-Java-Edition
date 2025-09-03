package com.d4rk.androidtutorials.java.ui.screens.android.lessons.reviews.ratingbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRatingBarBinding;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;

public class RatingBarActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private ActivityRatingBarBinding binding;
    private float rating = 0f;
    private String formattedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        formattedString = String.format(getString(R.string.stars), rating);
        binding.textViewRatingValue.setText(formattedString);
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            this.rating = rating;
            updateRatingText();
        });
        binding.button.setOnClickListener(v -> showRatingToast());
        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "RatingBar");
            startActivity(intent);
        });
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }

    private void updateRatingText() {
        formattedString = String.format(getString(R.string.stars), rating);
        binding.textViewRatingValue.setText(formattedString);
    }

    private void showRatingToast() {
        formattedString = String.format(getString(R.string.snack_rating), rating);
        Toast.makeText(this, formattedString, Toast.LENGTH_SHORT).show();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
