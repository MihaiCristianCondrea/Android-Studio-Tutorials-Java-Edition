package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.source.HomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource;

import org.junit.Test;

import java.util.List;

public class DefaultHomeRepositoryTest {

    private static class FakeHomeLocalDataSource implements HomeLocalDataSource {
        @Override
        public String getPlayStoreUrl() {
            return "play";
        }

        @Override
        public String getAppPlayStoreUrl(String packageName) {
            return "play/" + packageName;
        }

        @Override
        public String getDailyTip() {
            return "tip";
        }
    }

    private static class FakeHomeRemoteDataSource implements HomeRemoteDataSource {
        private final List<PromotedApp> apps;
        boolean called = false;

        FakeHomeRemoteDataSource(List<PromotedApp> apps) {
            this.apps = apps;
        }

        @Override
        public void fetchPromotedApps(PromotedAppsCallback callback) {
            called = true;
            callback.onResult(apps);
        }
    }

    @Test
    public void repositoryDelegatesToDataSources() {
        List<PromotedApp> promoted = List.of(new PromotedApp("Name", "pkg", "icon"));
        FakeHomeRemoteDataSource remote = new FakeHomeRemoteDataSource(promoted);
        FakeHomeLocalDataSource local = new FakeHomeLocalDataSource();

        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        assertEquals("play", repository.getPlayStoreUrl());
        assertEquals("play/pkg", repository.getAppPlayStoreUrl("pkg"));
        assertEquals("tip", repository.getDailyTip());

        final List<PromotedApp>[] result = new List[1]; // FIXME: Unchecked assignment: 'java.util.List[]' to 'java.util.List<com.d4rk.androidtutorials.java.data.model.PromotedApp>[]'
        repository.fetchPromotedApps(apps -> result[0] = apps);
        assertTrue(remote.called);
        assertEquals(promoted, result[0]);
    }
}
