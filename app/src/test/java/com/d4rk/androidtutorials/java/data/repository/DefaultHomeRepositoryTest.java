package com.d4rk.androidtutorials.java.data.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.source.HomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class DefaultHomeRepositoryTest {

    private static final List<PromotedApp> PROMOTED_APPS =
            List.of(new PromotedApp("Name", "pkg", "icon"));

    @Test
    public void getPlayStoreUrlDelegatesToLocalSource() {
        SpyHomeLocalDataSource local = new SpyHomeLocalDataSource();
        RecordingHomeRemoteDataSource remote =
                new RecordingHomeRemoteDataSource(Collections.emptyList());
        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        String result = repository.getPlayStoreUrl();

        assertEquals("play", result);
        assertTrue(local.playStoreUrlCalled);
        assertFalse(local.appPlayStoreUrlCalled);
        assertFalse(local.dailyTipCalled);
        assertFalse(remote.fetchCalled);
    }

    @Test
    public void getAppPlayStoreUrlDelegatesToLocalSource() {
        SpyHomeLocalDataSource local = new SpyHomeLocalDataSource();
        RecordingHomeRemoteDataSource remote =
                new RecordingHomeRemoteDataSource(Collections.emptyList());
        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        String packageName = "pkg";
        String result = repository.getAppPlayStoreUrl(packageName);

        assertEquals("play/pkg", result);
        assertTrue(local.appPlayStoreUrlCalled);
        assertEquals(packageName, local.lastRequestedPackage);
        assertFalse(local.playStoreUrlCalled);
        assertFalse(local.dailyTipCalled);
        assertFalse(remote.fetchCalled);
    }

    @Test
    public void dailyTipDelegatesToLocalSource() {
        SpyHomeLocalDataSource local = new SpyHomeLocalDataSource();
        RecordingHomeRemoteDataSource remote =
                new RecordingHomeRemoteDataSource(Collections.emptyList());
        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        String result = repository.dailyTip();

        assertEquals("tip", result);
        assertTrue(local.dailyTipCalled);
        assertFalse(local.playStoreUrlCalled);
        assertFalse(local.appPlayStoreUrlCalled);
        assertFalse(remote.fetchCalled);
    }

    @Test
    public void fetchPromotedAppsDelegatesToRemoteSource() {
        SpyHomeLocalDataSource local = new SpyHomeLocalDataSource();
        RecordingHomeRemoteDataSource remote =
                new RecordingHomeRemoteDataSource(List.of(PROMOTED_APPS));
        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        AtomicReference<List<PromotedApp>> result = new AtomicReference<>();
        repository.fetchPromotedApps(result::set);

        assertTrue(remote.fetchCalled);
        assertEquals(PROMOTED_APPS, result.get());
        assertFalse(local.playStoreUrlCalled);
        assertFalse(local.appPlayStoreUrlCalled);
        assertFalse(local.dailyTipCalled);
    }

    @Test
    public void fetchPromotedAppsEmitsOnlyFirstRemoteResponse() {
        SpyHomeLocalDataSource local = new SpyHomeLocalDataSource();
        List<PromotedApp> secondResponse =
                List.of(new PromotedApp("Second", "second.pkg", "secondIcon"));
        RecordingHomeRemoteDataSource remote =
                new RecordingHomeRemoteDataSource(List.of(PROMOTED_APPS, secondResponse));
        DefaultHomeRepository repository = new DefaultHomeRepository(remote, local);

        AtomicInteger callbackCount = new AtomicInteger();
        AtomicReference<List<PromotedApp>> result = new AtomicReference<>();
        repository.fetchPromotedApps(apps -> {
            callbackCount.incrementAndGet();
            result.set(apps);
        });

        assertTrue(remote.fetchCalled);
        assertEquals(1, callbackCount.get());
        assertEquals(PROMOTED_APPS, result.get());
    }

    private static class SpyHomeLocalDataSource implements HomeLocalDataSource {
        boolean playStoreUrlCalled;
        boolean appPlayStoreUrlCalled;
        boolean dailyTipCalled;
        String lastRequestedPackage;

        @Override
        public String getPlayStoreUrl() {
            playStoreUrlCalled = true;
            return "play";
        }

        @Override
        public String getAppPlayStoreUrl(String packageName) {
            appPlayStoreUrlCalled = true;
            lastRequestedPackage = packageName;
            return "play/" + packageName;
        }

        @Override
        public String getDailyTip() {
            dailyTipCalled = true;
            return "tip";
        }
    }

    private static class RecordingHomeRemoteDataSource implements HomeRemoteDataSource {
        private final List<List<PromotedApp>> responses;
        boolean fetchCalled;

        RecordingHomeRemoteDataSource(List<List<PromotedApp>> responses) {
            this.responses = responses;
        }

        @Override
        public void fetchPromotedApps(PromotedAppsCallback callback) {
            fetchCalled = true;
            for (List<PromotedApp> response : responses) {
                callback.onResult(response);
            }
        }
    }
}
