package com.d4rk.androidtutorials.java.ui.screens.main;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.domain.main.ApplyLanguageSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.ApplyThemeSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetAppUpdateManagerUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetBottomNavLabelVisibilityUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetDefaultTabPreferenceUseCase;
import com.d4rk.androidtutorials.java.domain.main.MarkStartupScreenShownUseCase;
import com.d4rk.androidtutorials.java.domain.main.ShouldShowStartupScreenUseCase;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.play.core.appupdate.AppUpdateManager;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for MainActivity. It interacts with MainRepository to retrieve or
 * update data, and exposes it to the UI.
 */
@HiltViewModel
public class MainViewModel extends ViewModel {

    private final ApplyThemeSettingsUseCase applyThemeSettingsUseCase;
    private final GetBottomNavLabelVisibilityUseCase getBottomNavLabelVisibilityUseCase;
    private final GetDefaultTabPreferenceUseCase getDefaultTabPreferenceUseCase;
    private final ApplyLanguageSettingsUseCase applyLanguageSettingsUseCase;
    private final ShouldShowStartupScreenUseCase shouldShowStartupScreenUseCase;
    private final MarkStartupScreenShownUseCase markStartupScreenShownUseCase;
    private final GetAppUpdateManagerUseCase getAppUpdateManagerUseCase;
    private final MutableLiveData<MainUiState> uiState = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    @Inject
    public MainViewModel(ApplyThemeSettingsUseCase applyThemeSettingsUseCase,
                         GetBottomNavLabelVisibilityUseCase getBottomNavLabelVisibilityUseCase,
                         GetDefaultTabPreferenceUseCase getDefaultTabPreferenceUseCase,
                         ApplyLanguageSettingsUseCase applyLanguageSettingsUseCase,
                         ShouldShowStartupScreenUseCase shouldShowStartupScreenUseCase,
                         MarkStartupScreenShownUseCase markStartupScreenShownUseCase,
                         GetAppUpdateManagerUseCase getAppUpdateManagerUseCase) {
        this.applyThemeSettingsUseCase = applyThemeSettingsUseCase;
        this.getBottomNavLabelVisibilityUseCase = getBottomNavLabelVisibilityUseCase;
        this.getDefaultTabPreferenceUseCase = getDefaultTabPreferenceUseCase;
        this.applyLanguageSettingsUseCase = applyLanguageSettingsUseCase;
        this.shouldShowStartupScreenUseCase = shouldShowStartupScreenUseCase;
        this.markStartupScreenShownUseCase = markStartupScreenShownUseCase;
        this.getAppUpdateManagerUseCase = getAppUpdateManagerUseCase;
    }

    private static @NavigationBarView.LabelVisibility int getVisibilityMode(String labelVisibilityStr, String[] bottomNavBarLabelsValues) {
        @NavigationBarView.LabelVisibility int visibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO;
        if (labelVisibilityStr.equals(bottomNavBarLabelsValues[0])) {
            visibilityMode = NavigationBarView.LABEL_VISIBILITY_LABELED;
        } else if (labelVisibilityStr.equals(bottomNavBarLabelsValues[1])) {
            visibilityMode = NavigationBarView.LABEL_VISIBILITY_SELECTED;
        } else if (labelVisibilityStr.equals(bottomNavBarLabelsValues[2])) {
            visibilityMode = NavigationBarView.LABEL_VISIBILITY_UNLABELED;
        }
        return visibilityMode;
    }

    /**
     * Loads and applies settings such as theme, bottom nav visibility, default tab, etc.
     * This can be called from onCreate() or similar lifecycle methods in MainActivity.
     */
    public void applySettings(String[] themeValues,
                              String[] bottomNavBarLabelsValues,
                              String[] defaultTabValues) {
        isLoading.setValue(true);
        boolean changedTheme = applyThemeSettingsUseCase.invoke(themeValues);

        String labelVisibilityStr = getBottomNavLabelVisibilityUseCase.invoke();
        @NavigationBarView.LabelVisibility int visibilityMode = getVisibilityMode(labelVisibilityStr, bottomNavBarLabelsValues);

        String startFragmentIdValue = getDefaultTabPreferenceUseCase.invoke();
        int startFragmentId;
        if (startFragmentIdValue.equals(defaultTabValues[0])) {
            startFragmentId = R.id.navigation_home;
        } else if (startFragmentIdValue.equals(defaultTabValues[1])) {
            startFragmentId = R.id.navigation_android_studio;
        } else if (startFragmentIdValue.equals(defaultTabValues[2])) {
            startFragmentId = R.id.navigation_about;
        } else {
            startFragmentId = R.id.navigation_home;
        }

        uiState.setValue(new MainUiState(visibilityMode, startFragmentId, changedTheme));
        applyLanguageSettingsUseCase.invoke();
        isLoading.setValue(false);
    }

    /**
     * Checks if we need to show the startup screen.
     */
    public boolean shouldShowStartupScreen() { // FIXME: Method 'shouldShowStartupScreen()' is never used
        return shouldShowStartupScreenUseCase.invoke();
    }

    /**
     * Mark startup screen as shown.
     */
    public void markStartupScreenShown() { // FIXME: Method 'markStartupScreenShown()' is never used
        markStartupScreenShownUseCase.invoke();
    }

    /**
     * Expose the consolidated UI state so MainActivity can observe it.
     */
    public LiveData<MainUiState> getUiState() {
        return uiState;
    }

    /**
     * Expose loading state to toggle progress indicators.
     */
    public LiveData<Boolean> getLoadingState() {
        return isLoading;
    }

    /**
     * Expose the AppUpdateManager if the Activity wants to directly check for in-app updates.
     */
    public AppUpdateManager getAppUpdateManager() {
        return getAppUpdateManagerUseCase.invoke();
    }
}