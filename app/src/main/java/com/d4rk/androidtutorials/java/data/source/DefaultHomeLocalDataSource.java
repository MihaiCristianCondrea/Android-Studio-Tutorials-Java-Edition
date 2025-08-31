package com.d4rk.androidtutorials.java.data.source;

import android.content.Context;

import com.d4rk.androidtutorials.java.R;

/**
 * Default implementation that reads from Android resources.
 */
public class DefaultHomeLocalDataSource implements HomeLocalDataSource {

    private final Context context;

    public DefaultHomeLocalDataSource(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public String getPlayStoreUrl() {
        return "https://play.google.com/store/apps/details?id=com.d4rk.androidtutorials";
    }

    @Override
    public String getAppPlayStoreUrl(String packageName) {
        return "https://play.google.com/store/apps/details?id=" + packageName;
    }

    @Override
    public String getDailyTip() {
        String[] tips = context.getResources().getStringArray(R.array.daily_tips);
        long daysSinceEpoch = System.currentTimeMillis() / (24L * 60 * 60 * 1000);
        int index = (int) (daysSinceEpoch % tips.length);
        return tips[index];
    }

}
