package com.d4rk.androidtutorials.java.ui.screens.about;

import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.domain.about.GetCurrentYearUseCase;
import com.d4rk.androidtutorials.java.domain.about.GetVersionStringUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;


/**
 * ViewModel for the About screen. Delegates data/logic to AboutRepository.
 */
@HiltViewModel
public class AboutViewModel extends ViewModel {

    private final GetVersionStringUseCase getVersionStringUseCase;
    private final GetCurrentYearUseCase getCurrentYearUseCase;

    @Inject
    public AboutViewModel(GetVersionStringUseCase getVersionStringUseCase,
                          GetCurrentYearUseCase getCurrentYearUseCase) {
        this.getVersionStringUseCase = getVersionStringUseCase;
        this.getCurrentYearUseCase = getCurrentYearUseCase;
    }

    /**
     * Returns a formatted version string, e.g. "v1.2.3 (45)".
     */
    public String getVersionString() {
        return getVersionStringUseCase.invoke();
    }

    /**
     * Returns the current year, e.g. "2024".
     */
    public String getCurrentYear() {
        return getCurrentYearUseCase.invoke();
    }
}
