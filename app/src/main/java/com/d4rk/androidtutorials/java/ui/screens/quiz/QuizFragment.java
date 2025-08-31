package com.d4rk.androidtutorials.java.ui.screens.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.airbnb.lottie.LottieAnimationView;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.model.QuizQuestion;
import com.d4rk.androidtutorials.java.databinding.FragmentQuizBinding;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private QuizViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(requireActivity());
        edgeToEdgeDelegate.applyEdgeToEdge(binding.container);

        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);
        if (viewModel.getTotalQuestions() == 0) {
            new MaterialAlertDialogBuilder(requireContext())
                    .setMessage(R.string.quiz_no_more_questions)
                    .setPositiveButton(android.R.string.ok, (d, w) -> NavHostFragment.findNavController(this).popBackStack())
                    .setCancelable(false)
                    .show();
        } else {
            showQuestion(viewModel.getCurrentQuestion());
            binding.buttonNext.setOnClickListener(v -> onNextClicked());
        }

        return binding.getRoot();
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
        if (viewModel.getCurrentIndex().getValue() >= viewModel.getTotalQuestions()) {
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
        int score = viewModel.getScore().getValue();
        int total = viewModel.getTotalQuestions();
        View view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_quiz_result, null, false);
        TextView textResult = view.findViewById(R.id.text_result);
        textResult.setText(getString(R.string.quiz_finished, score, total));
        LottieAnimationView animationView = view.findViewById(R.id.animation_success);
        animationView.playAnimation();
        new MaterialAlertDialogBuilder(requireContext())
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> NavHostFragment.findNavController(this).popBackStack())
                .setCancelable(false)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
