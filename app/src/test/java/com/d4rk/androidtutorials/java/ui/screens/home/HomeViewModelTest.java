package com.d4rk.androidtutorials.java.ui.screens.home;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.repository.HomeRepository;
import com.d4rk.androidtutorials.java.domain.home.GetAppPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetDailyTipUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPlayStoreUrlUseCase;
import com.d4rk.androidtutorials.java.domain.home.GetPromotedAppsUseCase;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HomeViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void uiStateUpdatesWithData() {
        List<PromotedApp> promoted = List.of(new PromotedApp("App", "pkg", "icon"));
        FakeHomeRepository repo = new FakeHomeRepository(promoted);
        HomeViewModel viewModel = createViewModel(repo);
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
        HomeViewModel viewModel = createViewModel(repo);
        viewModel.setAnnouncements("Title", "Subtitle");

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertTrue(state.promotedApps().isEmpty());
    }

    @Test
    public void promotedAppsLimitedToAtMostFour() {
        List<PromotedApp> promoted = List.of(
                new PromotedApp("App1", "pkg1", "icon1"),
                new PromotedApp("App2", "pkg2", "icon2"),
                new PromotedApp("App3", "pkg3", "icon3"),
                new PromotedApp("App4", "pkg4", "icon4"),
                new PromotedApp("App5", "pkg5", "icon5")
        );
        FakeHomeRepository repo = new FakeHomeRepository(promoted);
        HomeViewModel viewModel = createViewModel(repo);

        HomeUiState state = viewModel.getUiState().getValue();
        assertNotNull(state);
        assertTrue(state.promotedApps().size() <= 4);
    }

    @Test
    public void getOpenPlayStoreIntent_buildsViewIntent() {
        FakeHomeRepository repo = new FakeHomeRepository(List.of());
        String expectedUrl = "https://play.google.com/store/apps/details?id=com.example";
        repo.setPlayStoreUrl(expectedUrl);
        HomeViewModel viewModel = createViewModel(repo);

        Intent intent = viewModel.getOpenPlayStoreIntent();

        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals(Uri.parse(expectedUrl), intent.getData());
    }

    @Test
    public void getPromotedAppIntent_buildsViewIntent() {
        FakeHomeRepository repo = new FakeHomeRepository(List.of());
        String packageName = "pkg";
        String expectedUrl = "https://play.google.com/store/apps/details?id=" + packageName;
        repo.setAppUrl(packageName, expectedUrl);
        HomeViewModel viewModel = createViewModel(repo);

        Intent intent = viewModel.getPromotedAppIntent(packageName);

        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals(Uri.parse(expectedUrl), intent.getData());
    }

    @Test
    public void getLearnMoreIntent_targetsAndroidDevelopers() {
        FakeHomeRepository repo = new FakeHomeRepository(List.of());
        HomeViewModel viewModel = createViewModel(repo);

        Intent intent = viewModel.getLearnMoreIntent();

        assertEquals(Intent.ACTION_VIEW, intent.getAction());
        assertEquals(Uri.parse("https://developer.android.com"), intent.getData());
    }

    private HomeViewModel createViewModel(FakeHomeRepository repo) {
        return new HomeViewModel(
                new GetDailyTipUseCase(repo),
                new GetPromotedAppsUseCase(repo),
                new GetPlayStoreUrlUseCase(repo),
                new GetAppPlayStoreUrlUseCase(repo)
        );
    }

    private static final class FakeHomeRepository implements HomeRepository {
        private final List<PromotedApp> promotedApps;
        private String dailyTip = "tip";
        private String playStoreUrl = "https://play.google.com/store/apps/details?id=default";
        private final Map<String, String> appUrls = new HashMap<>();

        FakeHomeRepository(List<PromotedApp> promotedApps) {
            this.promotedApps = new ArrayList<>(promotedApps);
        }

        void setDailyTip(String dailyTip) {
            this.dailyTip = dailyTip;
        }

        void setPlayStoreUrl(String playStoreUrl) {
            this.playStoreUrl = playStoreUrl;
        }

        void setAppUrl(String packageName, String url) {
            appUrls.put(packageName, url);
        }

        @Override
        public String dailyTip() {
            return dailyTip;
        }

        @Override
        public String getPlayStoreUrl() {
            return playStoreUrl;
        }

        @Override
        public String getAppPlayStoreUrl(String packageName) {
            return appUrls.getOrDefault(packageName, "");
        }

        @Override
        public void fetchPromotedApps(PromotedAppsCallback callback) {
            callback.onResult(promotedApps);
        }
    }
}
