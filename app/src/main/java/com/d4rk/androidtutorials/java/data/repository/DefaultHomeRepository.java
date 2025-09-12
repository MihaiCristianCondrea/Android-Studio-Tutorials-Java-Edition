package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.source.HomeLocalDataSource;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource;

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
    public String getPlayStoreUrl() {
        return localDataSource.getPlayStoreUrl();
    }

    @Override
    public String getAppPlayStoreUrl(String packageName) {
        return localDataSource.getAppPlayStoreUrl(packageName);
    }

    @Override
    public String getDailyTip() {
        return localDataSource.getDailyTip();
    }

    @Override
    public void fetchPromotedApps(PromotedAppsCallback callback) {
        remoteDataSource.fetchPromotedApps(callback::onResult);
    }
}
