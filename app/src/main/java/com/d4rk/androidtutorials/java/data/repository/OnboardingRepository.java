package com.d4rk.androidtutorials.java.data.repository;

public interface OnboardingRepository {
    boolean isOnboardingComplete();
    void setOnboardingComplete();
    void setTheme(String value);
    void setDefaultTab(String value);
    void setBottomBarLabels(String value);
}
