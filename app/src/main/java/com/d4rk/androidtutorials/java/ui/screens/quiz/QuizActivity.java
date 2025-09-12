package com.d4rk.androidtutorials.java.ui.screens.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.model.QuizQuestion;
import com.d4rk.androidtutorials.java.databinding.ActivityQuizBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuizActivity extends AppCompatActivity {

    private ActivityQuizBinding binding;
    private QuizViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        if (viewModel.getTotalQuestions() == 0) {
            new MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.quiz_no_more_questions)
                    .setPositiveButton(android.R.string.ok, (d, w) -> finish())
                    .setCancelable(false)
                    .show();
            return;
        }
        showQuestion(viewModel.getCurrentQuestion());

        binding.buttonNext.setOnClickListener(v -> onNextClicked());
    }

    private void onNextClicked() {
        int selectedId = binding.optionsGroup.getCheckedRadioButtonId();
        int selectedIndex = -1;
        if (selectedId == binding.option1.getId()) {
            selectedIndex = 0;
        } else if (selectedId == binding.option2.getId()) {
            selectedIndex = 1;
        } else if (selectedId == binding.option3.getId()) {
            selectedIndex = 2;
        } else if (selectedId == binding.option4.getId()) {
            selectedIndex = 3;
        }
        if (selectedIndex != -1) {
            viewModel.answer(selectedIndex);
        }
        if (viewModel.getCurrentIndex().getValue() >= viewModel.getTotalQuestions()) { // FIXME: Unboxing of 'viewModel.getCurrentIndex().getValue()' may produce 'NullPointerException'
            showResult();
        } else {
            showQuestion(viewModel.getCurrentQuestion());
            binding.optionsGroup.clearCheck();
        }
    }

    private void showQuestion(QuizQuestion question) {
        if (question == null) {
            return;
        }
        binding.textQuestion.setText(question.question());
        binding.option1.setText(question.options()[0]);
        binding.option2.setText(question.options()[1]);
        binding.option3.setText(question.options()[2]);
        binding.option4.setText(question.options()[3]);
    }

    private void showResult() {
        int score = viewModel.getScore().getValue(); // FIXME: Unboxing of 'viewModel.getScore().getValue()' may produce 'NullPointerException'
        int total = viewModel.getTotalQuestions();
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_quiz_result, null, false);
        TextView textResult = view.findViewById(R.id.text_result);
        textResult.setText(getString(R.string.quiz_finished, score, total));
        LottieAnimationView animationView = view.findViewById(R.id.animation_success);
        animationView.playAnimation();
        new MaterialAlertDialogBuilder(this)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
