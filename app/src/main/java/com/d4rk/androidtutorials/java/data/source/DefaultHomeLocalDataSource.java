package com.d4rk.androidtutorials.java.data.source;

import android.content.Context;

import com.d4rk.androidtutorials.java.R;

/**
 * Default implementation that reads from Android resources.
 */
public class DefaultHomeLocalDataSource implements HomeLocalDataSource {

    private static final String PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id=";

    private final Context context;

    public DefaultHomeLocalDataSource(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public String getPlayStoreUrl() {
        return PLAY_STORE_BASE_URL;
    }

    @Override
    public String getAppPlayStoreUrl(String packageName) {
        if (packageName == null) {
            return PLAY_STORE_BASE_URL;
        }
        return PLAY_STORE_BASE_URL + packageName;
    }

    @Override
    public String getDailyTip() {
        String[] tips = context.getResources().getStringArray(R.array.daily_tips);
        long daysSinceEpoch = System.currentTimeMillis() / (24L * 60 * 60 * 1000);
        int index = (int) (daysSinceEpoch % tips.length);
        return tips[index];
    }

}
