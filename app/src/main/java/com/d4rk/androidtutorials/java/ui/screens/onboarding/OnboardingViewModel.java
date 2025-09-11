package com.d4rk.androidtutorials.java.ui.screens.onboarding;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.data.repository.OnboardingRepository;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

@HiltViewModel
public class OnboardingViewModel extends ViewModel {

    private final OnboardingRepository repository;

    @Inject
    public OnboardingViewModel(OnboardingRepository repository) {
        this.repository = repository;
    }

    public void saveTheme(String value) {
        repository.setTheme(value);
    }

    public void saveDefaultTab(String value) {
        repository.setDefaultTab(value);
    }

    public void saveBottomBarLabels(String value) {
        repository.setBottomBarLabels(value);
    }

    public void completeOnboarding() {
        repository.setOnboardingComplete();
    }
}
