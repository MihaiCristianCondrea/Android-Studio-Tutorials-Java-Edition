package com.d4rk.androidtutorials.java.data.repository;

import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import java.util.List;
import java.util.function.Consumer;

/**
 * Abstraction over home data operations.
 */
public interface HomeRepository {

    String getPlayStoreUrl();

    String getAppPlayStoreUrl(String packageName);

    String getDailyTip();

    void fetchPromotedApps(Consumer<List<PromotedApp>> callback);
}
