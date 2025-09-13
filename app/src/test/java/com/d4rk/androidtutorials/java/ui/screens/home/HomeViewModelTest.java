package com.d4rk.androidtutorials.java.ui.screens.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;
import com.d4rk.androidtutorials.java.domain.home.GetAppPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;


public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void uiStateUpdatesWithData() {
        List<PromotedApp> promoted = List.of(new PromotedApp("App", "pkg", "icon"));
        FakeHomeRepository repo = new FakeHomeRepository(promoted);
        HomeViewModel viewModel = new HomeViewModel(
                new GetDailyTipUseCase(repo),
                new GetPromotedAppsUseCase(repo),
                new GetPlayStoreUrlUseCase(repo),
                new GetAppPlayStoreUrlUseCase(repo)
        );
        viewModel.setAnnouncements("Title", "Subtitle");

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertEquals("Title", state.announcementTitle());
        assertEquals("Subtitle", state.announcementSubtitle());
        assertEquals("tip", state.dailyTip());
        assertEquals(promoted, state.promotedApps());
    }

    @Test
    public void uiStateHandlesEmptyPromotedApps() {
        FakeHomeRepository repo = new FakeHomeRepository(List.of());
        HomeViewModel viewModel = new HomeViewModel(
                new GetDailyTipUseCase(repo),
                new GetPromotedAppsUseCase(repo),
                new GetPlayStoreUrlUseCase(repo),
                new GetAppPlayStoreUrlUseCase(repo)
        );
        viewModel.setAnnouncements("Title", "Subtitle");

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertTrue(state.promotedApps().isEmpty());
    }

    record FakeHomeRepository(List<PromotedApp> apps) implements HomeRepository {

        @Override
        public String dailyTip() {
            return "tip";
        }

        @Override
        public String getPlayStoreUrl() {
            return "";
        }

        @Override
        public String getAppPlayStoreUrl(String packageName) {
            return "";
        }

        @Override
        public void fetchPromotedApps(PromotedAppsCallback callback) {
            callback.onResult(apps);
        }
    }
}
