package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.source.HomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultHomeRepositoryTest {

    @Test
    public void repositoryDelegatesToDataSources() {
        List<PromotedApp> promoted = List.of(new PromotedApp("Name", "pkg", "icon"));
        FakeHomeRemoteDataSource remote = new FakeHomeRemoteDataSource(promoted);
        FakeHomeLocalDataSource local = new FakeHomeLocalDataSource();

        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        assertEquals("play", repository.getPlayStoreUrl());
        assertEquals("play/pkg", repository.getAppPlayStoreUrl("pkg"));
        assertEquals("tip", repository.getDailyTip());

        AtomicReference<List<PromotedApp>> result = new AtomicReference<>();
        repository.fetchPromotedApps(result::set);
        assertTrue(remote.called);
        assertEquals(promoted, result.get());
    }

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
}
