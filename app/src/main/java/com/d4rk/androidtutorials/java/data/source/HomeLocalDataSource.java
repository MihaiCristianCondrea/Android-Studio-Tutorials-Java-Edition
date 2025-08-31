package com.d4rk.androidtutorials.java.data.source;

/**
 * Local data access for the home feature.
 */
public interface HomeLocalDataSource {

    String getPlayStoreUrl();

    String getAppPlayStoreUrl(String packageName);

    String getDailyTip();
}
