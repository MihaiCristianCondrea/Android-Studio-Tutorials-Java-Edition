package com.d4rk.androidtutorials.java.ui.screens.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.domain.main.ApplyLanguageSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.ApplyThemeSettingsUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetAppUpdateManagerUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetBottomNavLabelVisibilityUseCase;
import com.d4rk.androidtutorials.java.domain.main.GetDefaultTabPreferenceUseCase;
import com.d4rk.androidtutorials.java.domain.main.MarkStartupScreenShownUseCase;
import com.d4rk.androidtutorials.java.domain.main.ShouldShowStartupScreenUseCase;
import com.google.android.material.navigation.NavigationBarView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private ApplyThemeSettingsUseCase applyThemeSettingsUseCase;
    private GetBottomNavLabelVisibilityUseCase getBottomNavLabelVisibilityUseCase;
    private GetDefaultTabPreferenceUseCase getDefaultTabPreferenceUseCase;
    private ApplyLanguageSettingsUseCase applyLanguageSettingsUseCase;
    private ShouldShowStartupScreenUseCase shouldShowStartupScreenUseCase;
    private MarkStartupScreenShownUseCase markStartupScreenShownUseCase;
    private GetAppUpdateManagerUseCase getAppUpdateManagerUseCase;
    private MainViewModel viewModel;

    private final String[] themeValues = {"MODE_NIGHT_FOLLOW_SYSTEM", "MODE_NIGHT_NO", "MODE_NIGHT_YES"};
    private final String[] bottomNavBarLabelsValues = {"labeled", "selected", "unlabeled"};
    private final String[] defaultTabValues = {"home", "android_studio", "about"};

    @Before
    public void setUp() {
        applyThemeSettingsUseCase = Mockito.mock(ApplyThemeSettingsUseCase.class);
        getBottomNavLabelVisibilityUseCase = Mockito.mock(GetBottomNavLabelVisibilityUseCase.class);
        getDefaultTabPreferenceUseCase = Mockito.mock(GetDefaultTabPreferenceUseCase.class);
        applyLanguageSettingsUseCase = Mockito.mock(ApplyLanguageSettingsUseCase.class);
        shouldShowStartupScreenUseCase = Mockito.mock(ShouldShowStartupScreenUseCase.class);
        markStartupScreenShownUseCase = Mockito.mock(MarkStartupScreenShownUseCase.class);
        getAppUpdateManagerUseCase = Mockito.mock(GetAppUpdateManagerUseCase.class);

        viewModel = new MainViewModel(
                applyThemeSettingsUseCase,
                getBottomNavLabelVisibilityUseCase,
                getDefaultTabPreferenceUseCase,
                applyLanguageSettingsUseCase,
                shouldShowStartupScreenUseCase,
                markStartupScreenShownUseCase,
                getAppUpdateManagerUseCase
        );
    }

    @Test
    public void applySettingsUpdatesUiStateForLabeledHome() {
        Mockito.when(applyThemeSettingsUseCase.invoke(themeValues)).thenReturn(true);
        Mockito.when(getBottomNavLabelVisibilityUseCase.invoke()).thenReturn("labeled");
        Mockito.when(getDefaultTabPreferenceUseCase.invoke()).thenReturn("home");

        List<Boolean> loading = new ArrayList<>();
        viewModel.getLoadingState().observeForever(loading::add);

        viewModel.applySettings(themeValues, bottomNavBarLabelsValues, defaultTabValues);

        assertEquals(List.of(false, true, false), loading);

        MainUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertEquals(NavigationBarView.LABEL_VISIBILITY_LABELED, state.bottomNavVisibility());
        assertEquals(R.id.navigation_home, state.defaultNavDestination());
        assertTrue(state.themeChanged());
    }

    @Test
    public void applySettingsUpdatesUiStateForSelectedAndroidStudio() {
        Mockito.when(applyThemeSettingsUseCase.invoke(themeValues)).thenReturn(false);
        Mockito.when(getBottomNavLabelVisibilityUseCase.invoke()).thenReturn("selected");
        Mockito.when(getDefaultTabPreferenceUseCase.invoke()).thenReturn("android_studio");

        List<Boolean> loading = new ArrayList<>();
        viewModel.getLoadingState().observeForever(loading::add);

        viewModel.applySettings(themeValues, bottomNavBarLabelsValues, defaultTabValues);

        assertEquals(List.of(false, true, false), loading);

        MainUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertEquals(NavigationBarView.LABEL_VISIBILITY_SELECTED, state.bottomNavVisibility());
        assertEquals(R.id.navigation_android_studio, state.defaultNavDestination());
        assertFalse(state.themeChanged());
    }

    @Test
    public void applySettingsUpdatesUiStateForUnlabeledAbout() {
        Mockito.when(applyThemeSettingsUseCase.invoke(themeValues)).thenReturn(true);
        Mockito.when(getBottomNavLabelVisibilityUseCase.invoke()).thenReturn("unlabeled");
        Mockito.when(getDefaultTabPreferenceUseCase.invoke()).thenReturn("about");

        List<Boolean> loading = new ArrayList<>();
        viewModel.getLoadingState().observeForever(loading::add);

        viewModel.applySettings(themeValues, bottomNavBarLabelsValues, defaultTabValues);

        assertEquals(List.of(false, true, false), loading);

        MainUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertEquals(NavigationBarView.LABEL_VISIBILITY_UNLABELED, state.bottomNavVisibility());
        assertEquals(R.id.navigation_about, state.defaultNavDestination());
        assertTrue(state.themeChanged());
    }

    @Test
    public void shouldShowStartupScreenDelegatesToUseCase() {
        Mockito.when(shouldShowStartupScreenUseCase.invoke()).thenReturn(false);

        assertFalse(viewModel.shouldShowStartupScreen());
        Mockito.verify(shouldShowStartupScreenUseCase).invoke();
    }

    @Test
    public void markStartupScreenShownDelegatesToUseCase() {
        viewModel.markStartupScreenShown();

        Mockito.verify(markStartupScreenShownUseCase).invoke();
    }

    private int callVisibilityMode(String value) throws Exception {
        Method method = MainViewModel.class.getDeclaredMethod(
                "getVisibilityMode", String.class, String[].class);
        method.setAccessible(true);
        return (Integer) method.invoke(null, value, bottomNavBarLabelsValues);
    }

    @Test
    public void getVisibilityModeReturnsLabeled() throws Exception {
        assertEquals(NavigationBarView.LABEL_VISIBILITY_LABELED, callVisibilityMode("labeled"));
    }

    @Test
    public void getVisibilityModeReturnsSelected() throws Exception {
        assertEquals(NavigationBarView.LABEL_VISIBILITY_SELECTED, callVisibilityMode("selected"));
    }

    @Test
    public void getVisibilityModeReturnsUnlabeled() throws Exception {
        assertEquals(NavigationBarView.LABEL_VISIBILITY_UNLABELED, callVisibilityMode("unlabeled"));
    }

    @Test
    public void getVisibilityModeReturnsAutoForUnknown() throws Exception {
        assertEquals(NavigationBarView.LABEL_VISIBILITY_AUTO, callVisibilityMode("other"));
    }
}

