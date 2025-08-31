package com.d4rk.androidtutorials.java.ui.screens.main;

import android.content.Intent;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.domain.main.ApplyThemeSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetBottomNavLabelVisibilityUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetDefaultTabPreferenceUseCase;
import com.d4rk.androidtutorials.java.domain.main.ApplyLanguageSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.ShouldShowStartupScreenUseCase;
import com.d4rk.androidtutorials.java.domain.main.MarkStartupScreenShownUseCase;
import com.d4rk.androidtutorials.java.domain.main.IsAppInstalledUseCase;
import com.d4rk.androidtutorials.java.domain.main.BuildShortcutIntentUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetAppUpdateManagerUseCase;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.play.core.appupdate.AppUpdateManager;

import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;

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
    private final IsAppInstalledUseCase isAppInstalledUseCase;
    private final BuildShortcutIntentUseCase buildShortcutIntentUseCase;
    private final GetAppUpdateManagerUseCase getAppUpdateManagerUseCase;
    private final MutableLiveData<MainUiState> uiState = new MutableLiveData<>();

    @Inject
    public MainViewModel(ApplyThemeSettingsUseCase applyThemeSettingsUseCase,
                         GetBottomNavLabelVisibilityUseCase getBottomNavLabelVisibilityUseCase,
                         GetDefaultTabPreferenceUseCase getDefaultTabPreferenceUseCase,
                         ApplyLanguageSettingsUseCase applyLanguageSettingsUseCase,
                         ShouldShowStartupScreenUseCase shouldShowStartupScreenUseCase,
                         MarkStartupScreenShownUseCase markStartupScreenShownUseCase,
                         IsAppInstalledUseCase isAppInstalledUseCase,
                         BuildShortcutIntentUseCase buildShortcutIntentUseCase,
                         GetAppUpdateManagerUseCase getAppUpdateManagerUseCase) {
        this.applyThemeSettingsUseCase = applyThemeSettingsUseCase;
        this.getBottomNavLabelVisibilityUseCase = getBottomNavLabelVisibilityUseCase;
        this.getDefaultTabPreferenceUseCase = getDefaultTabPreferenceUseCase;
        this.applyLanguageSettingsUseCase = applyLanguageSettingsUseCase;
        this.shouldShowStartupScreenUseCase = shouldShowStartupScreenUseCase;
        this.markStartupScreenShownUseCase = markStartupScreenShownUseCase;
        this.isAppInstalledUseCase = isAppInstalledUseCase;
        this.buildShortcutIntentUseCase = buildShortcutIntentUseCase;
        this.getAppUpdateManagerUseCase = getAppUpdateManagerUseCase;
    }

    private static int getVisibilityMode(String labelVisibilityStr, String[] bottomNavBarLabelsValues) {
        int visibilityMode = NavigationBarView.LABEL_VISIBILITY_AUTO;
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
        boolean changedTheme = applyThemeSettingsUseCase.invoke(themeValues);

        String labelVisibilityStr = getBottomNavLabelVisibilityUseCase.invoke();
        int visibilityMode = getVisibilityMode(labelVisibilityStr, bottomNavBarLabelsValues);

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
    }

    /**
     * Checks if we need to show the startup screen.
     */
    public boolean shouldShowStartupScreen() {
        return shouldShowStartupScreenUseCase.invoke();
    }

    /**
     * Mark startup screen as shown.
     */
    public void markStartupScreenShown() {
        markStartupScreenShownUseCase.invoke();
    }

    /**
     * Check if the “Android Tutorials” app is installed or not.
     */
    public boolean isAndroidTutorialsInstalled() {
        return isAppInstalledUseCase.invoke("com.d4rk.androidtutorials.java");
    }

    /**
     * Build the intent for the shortcut (opens app if installed, or fallback to the Play Store).
     */
    public Intent getShortcutIntent(boolean isInstalled) {
        return buildShortcutIntentUseCase.invoke(isInstalled);
    }

    /**
     * Expose the consolidated UI state so MainActivity can observe it.
     */
    public LiveData<MainUiState> getUiState() {
        return uiState;
    }

    /**
     * Expose the AppUpdateManager if the Activity wants to directly check for in-app updates.
     */
    public AppUpdateManager getAppUpdateManager() {
        return getAppUpdateManagerUseCase.invoke();
    }
}