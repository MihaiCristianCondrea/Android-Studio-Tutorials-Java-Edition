package com.d4rk.androidtutorials.java.data.repository;

import android.content.Intent;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.data.source.HomeRemoteDataSource.PromotedAppsCallback;

/**
 * Abstraction over home data operations.
 */
public interface HomeRepository {

    Intent getPlayStoreIntent();

    Intent getAppPlayStoreIntent(String packageName);

    String getDailyTip();

    void fetchPromotedApps(PromotedAppsCallback callback);
}
