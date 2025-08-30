package com.d4rk.androidtutorials.java.data.source;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import java.util.List;

/**
 * Contract for fetching home screen data from remote sources.
 */
public interface HomeRemoteDataSource {

    interface PromotedAppsCallback {
        void onResult(List<PromotedApp> apps);
    }

    void fetchPromotedApps(PromotedAppsCallback callback);
}
