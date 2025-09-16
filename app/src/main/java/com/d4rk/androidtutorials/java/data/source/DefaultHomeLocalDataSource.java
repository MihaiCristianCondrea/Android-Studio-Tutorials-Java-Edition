package com.d4rk.androidtutorials.java.data.source;

import android.content.Context;

import com.d4rk.androidtutorials.java.R;

import java.time.Clock;

/**
 * Default implementation that reads from Android resources.
 */
public class DefaultHomeLocalDataSource implements HomeLocalDataSource {

    private final Context context;
    private final Clock clock;

    public DefaultHomeLocalDataSource(Context context) {
        this(context, Clock.systemUTC());
    }

    DefaultHomeLocalDataSource(Context context, Clock clock) {
        this.context = context.getApplicationContext();
        this.clock = clock;
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
        if (tips.length == 0) {
            throw new IllegalStateException("No daily tips available");
        }
        long millisPerDay = 24L * 60L * 60L * 1000L;
        long daysSinceEpoch = clock.millis() / millisPerDay;
        int index = (int) (daysSinceEpoch % tips.length);
        return tips[index];
    }

}
