package com.d4rk.androidtutorials.java.data.repository;

import android.content.Intent;

import com.d4rk.androidtutorials.java.data.source.HomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource.PromotedAppsCallback;

/**
 * Default implementation of {@link HomeRepository} combining local and remote sources.
 */
public class DefaultHomeRepository implements HomeRepository {

    private final HomeRemoteDataSource remoteDataSource;
    private final HomeLocalDataSource localDataSource;

    public DefaultHomeRepository(HomeRemoteDataSource remoteDataSource,
                                 HomeLocalDataSource localDataSource) {
        this.remoteDataSource = remoteDataSource;
        this.localDataSource = localDataSource;
    }

    @Override
    public Intent getPlayStoreIntent() {
        return localDataSource.getPlayStoreIntent();
    }

    @Override
    public Intent getAppPlayStoreIntent(String packageName) {
        return localDataSource.getAppPlayStoreIntent(packageName);
    }

    @Override
    public String getDailyTip() {
        return localDataSource.getDailyTip();
    }

    @Override
    public void fetchPromotedApps(PromotedAppsCallback callback) {
        remoteDataSource.fetchPromotedApps(callback);
    }
}
