package com.d4rk.androidtutorials.java.data.source;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
    public Intent getPlayStoreIntent() {
        String playStoreUrl = "https://play.google.com/store/apps/details?id=com.d4rk.androidtutorials";
        return buildPlayStoreIntent(playStoreUrl);
    }

    @Override
    public Intent getAppPlayStoreIntent(String packageName) {
        String url = "https://play.google.com/store/apps/details?id=" + packageName;
        return buildPlayStoreIntent(url);
    }

    @Override
    public String getDailyTip() {
        String[] tips = context.getResources().getStringArray(R.array.daily_tips);
        long daysSinceEpoch = System.currentTimeMillis() / (24L * 60 * 60 * 1000);
        int index = (int) (daysSinceEpoch % tips.length);
        return tips[index];
    }

    private Intent buildPlayStoreIntent(String url) {
        Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        playStoreIntent.setPackage("com.android.vending");
        if (playStoreIntent.resolveActivity(context.getPackageManager()) != null) {
            return playStoreIntent;
        }
        return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    }
}
