package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;

import java.util.List;

/**
 * Abstraction over home data operations.
 */
public interface HomeRepository {

    String getPlayStoreUrl();

    String getAppPlayStoreUrl(String packageName);

    String dailyTip();

    void fetchPromotedApps(PromotedAppsCallback callback);

    interface PromotedAppsCallback {
        void onResult(List<PromotedApp> apps);
    }
}
