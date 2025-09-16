package com.d4rk.androidtutorials.java.ui.screens.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Intent;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.domain.home.GetAppPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private GetDailyTipUseCase getDailyTipUseCase;

    @Mock
    private GetPromotedAppsUseCase getPromotedAppsUseCase;

    @Mock
    private GetPlayStoreUrlUseCase getPlayStoreUrlUseCase;

    @Mock
    private GetAppPlayStoreUrlUseCase getAppPlayStoreUrlUseCase;

    private GetPromotedAppsUseCase.Callback capturedCallback;

    @Before
    public void setUp() {
        when(getDailyTipUseCase.invoke()).thenReturn("Daily tip");
        when(getPlayStoreUrlUseCase.invoke()).thenReturn("https://example.com/app");
        when(getAppPlayStoreUrlUseCase.invoke(anyString()))
                .thenAnswer(invocation -> "https://example.com/" + invocation.getArgument(0));
    }

    private void stubPromotedAppsInvocation() {
        reset(getPromotedAppsUseCase);
        doAnswer(invocation -> {
            capturedCallback = invocation.getArgument(0);
            return null;
        }).when(getPromotedAppsUseCase).invoke(any());
    }

    private HomeViewModel createViewModel() {
        capturedCallback = null;
        return new HomeViewModel(
                getDailyTipUseCase,
                getPromotedAppsUseCase,
                getPlayStoreUrlUseCase,
                getAppPlayStoreUrlUseCase
        );
    }

    private void emitPromotedApps(List<PromotedApp> apps) {
        assertNotNull("Promoted apps callback was not captured", capturedCallback);
        capturedCallback.onResult(apps);
    }

    @Test
    public void initialState_containsDailyTipAndEmptyPromotions() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();

        HomeUiState state = viewModel.getUiState().getValue();

        assertNotNull(state);
        assertEquals("", state.announcementTitle());
        assertEquals("", state.announcementSubtitle());
        assertEquals("Daily tip", state.dailyTip());
        assertTrue(state.promotedApps().isEmpty());
    }

    @Test
    public void setAnnouncements_updatesUiState() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();

        viewModel.setAnnouncements("Title", "Subtitle");

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertEquals("Title", state.announcementTitle());
        assertEquals("Subtitle", state.announcementSubtitle());
        assertEquals("Daily tip", state.dailyTip());
    }

    @Test
    public void promotedAppsSuccess_updatesUiStateWithFilteredList() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();
        List<PromotedApp> source = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            source.add(new PromotedApp("App" + i, "pkg" + i, "icon" + i));
        }

        emitPromotedApps(source);

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        List<PromotedApp> promotedApps = state.promotedApps();
        assertTrue(promotedApps.size() <= 4);
        for (PromotedApp app : promotedApps) {
            assertTrue(source.contains(app));
        }
    }

    @Test
    public void promotedAppsEmpty_keepsListEmpty() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();

        emitPromotedApps(List.of());

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertTrue(state.promotedApps().isEmpty());
    }

    @Test
    public void getOpenPlayStoreIntent_usesUrlFromUseCase() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();

        Intent intent = viewModel.getOpenPlayStoreIntent();

        assertEquals(Uri.parse("https://example.com/app"), intent.getData());
        verify(getPlayStoreUrlUseCase).invoke();
    }

    @Test
    public void getPromotedAppIntent_usesPackageUrl() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();

        Intent intent = viewModel.getPromotedAppIntent("pkg");

        assertEquals(Uri.parse("https://example.com/pkg"), intent.getData());
        verify(getAppPlayStoreUrlUseCase).invoke("pkg");
    }

    @Test
    public void getPromotedAppIntent_propagatesFailure() {
        stubPromotedAppsInvocation();
        HomeViewModel viewModel = createViewModel();
        doThrow(new IllegalStateException("error"))
                .when(getAppPlayStoreUrlUseCase).invoke("broken");

        assertThrows(IllegalStateException.class, () -> viewModel.getPromotedAppIntent("broken"));
    }

    @Test
    public void constructor_propagatesPromotedAppsFailure() {
        doThrow(new RuntimeException("boom"))
                .when(getPromotedAppsUseCase).invoke(any());

        assertThrows(RuntimeException.class, () -> new HomeViewModel(
                getDailyTipUseCase,
                getPromotedAppsUseCase,
                getPlayStoreUrlUseCase,
                getAppPlayStoreUrlUseCase
        ));
    }
}

