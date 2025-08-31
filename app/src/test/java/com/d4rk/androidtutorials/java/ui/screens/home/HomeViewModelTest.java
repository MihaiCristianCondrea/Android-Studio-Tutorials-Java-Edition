package com.d4rk.androidtutorials.java.ui.screens.home;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import org.mockito.Mockito;

public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();


    static class FakeHomeRepository implements HomeRepository {
        final String dailyTip;
        final List<PromotedApp> apps;

        FakeHomeRepository(String dailyTip, List<PromotedApp> apps) {
            this.dailyTip = dailyTip;
            this.apps = apps;
        }

        @Override public String getPlayStoreUrl() { return ""; }
        @Override public String getAppPlayStoreUrl(String packageName) { return ""; }
        @Override public String getDailyTip() { return dailyTip; }
        @Override public void fetchPromotedApps(PromotedAppsCallback callback) { callback.onResult(apps); }
    }

    @Test
    public void uiStateUpdatesWithData() {
        List<PromotedApp> promoted = List.of(new PromotedApp("App", "pkg", "icon"));
        FakeHomeRepository repo = new FakeHomeRepository("tip", promoted);
        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getString(R.string.announcement_title)).thenReturn("Title");
        Mockito.when(app.getString(R.string.announcement_subtitle)).thenReturn("Subtitle");
        HomeViewModel viewModel = new HomeViewModel(app, repo,
                new GetDailyTipUseCase(repo), new GetPromotedAppsUseCase(repo));

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertEquals("Title", state.announcementTitle());
        assertEquals("Subtitle", state.announcementSubtitle());
        assertEquals("tip", state.dailyTip());
        assertEquals(promoted, state.promotedApps());
    }

    @Test
    public void uiStateHandlesEmptyPromotedApps() {
        FakeHomeRepository repo = new FakeHomeRepository("tip", List.of());
        Application app = Mockito.mock(Application.class);
        Mockito.when(app.getString(R.string.announcement_title)).thenReturn("Title");
        Mockito.when(app.getString(R.string.announcement_subtitle)).thenReturn("Subtitle");
        HomeViewModel viewModel = new HomeViewModel(app, repo,
                new GetDailyTipUseCase(repo), new GetPromotedAppsUseCase(repo));

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertTrue(state.promotedApps().isEmpty());
    }
}
