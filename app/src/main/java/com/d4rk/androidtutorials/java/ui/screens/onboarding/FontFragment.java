package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.FragmentOnboardingFontBinding;
import com.google.android.material.radiobutton.MaterialRadioButton;

public class FontFragment extends Fragment {

    private FragmentOnboardingFontBinding binding;
    private OnboardingViewModel viewModel;
    private MaterialRadioButton[] radioButtons;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingFontBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(OnboardingViewModel.class);

        radioButtons = new MaterialRadioButton[]{
                binding.optionAudiowide.radioButton,
                binding.optionFiraCode.radioButton,
                binding.optionJetbrainsMono.radioButton,
                binding.optionNotoSansMono.radioButton,
                binding.optionPoppins.radioButton,
                binding.optionRobotoMono.radioButton,
                binding.optionGoogleSansCode.radioButton
        };

        View[] optionCards = new View[]{
                binding.cardAudiowide,
                binding.cardFiraCode,
                binding.cardJetbrainsMono,
                binding.cardNotoSansMono,
                binding.cardPoppins,
                binding.cardRobotoMono,
                binding.cardGoogleSansCode
        };

        binding.optionAudiowide.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_audiowide));
        binding.optionFiraCode.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_fira_code));
        binding.optionJetbrainsMono.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_jetbrains_mono));
        binding.optionNotoSansMono.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_noto_sans_mono));
        binding.optionPoppins.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_poppins));
        binding.optionRobotoMono.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_roboto_mono));
        binding.optionGoogleSansCode.titleText.setTypeface(ResourcesCompat.getFont(requireContext(), R.font.font_google_sans_code));

        for (int i = 0; i < radioButtons.length; i++) {
            int index = i;
            radioButtons[i].setId(View.generateViewId());
            optionCards[i].setOnClickListener(v -> selectOption(index));
            radioButtons[i].setOnClickListener(v -> selectOption(index));
        }

        String current = viewModel.getMonospaceFont();
        String[] values = getResources().getStringArray(R.array.code_font_values);
        int index = 6;
        for (int i = 0; i < values.length; i++) {
            if (current.equals(values[i])) {
                index = i;
                break;
            }
        }
        selectOption(index);
    }

    private void selectOption(int index) {
        for (int i = 0; i < radioButtons.length; i++) {
            radioButtons[i].setChecked(i == index);
        }
    }

    public void saveSelection() {
        String[] values = getResources().getStringArray(R.array.code_font_values);
        int index = 6;
        for (int i = 0; i < radioButtons.length; i++) {
            if (radioButtons[i].isChecked()) {
                index = i;
                break;
            }
        }
        viewModel.setMonospaceFont(values[index]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
