package com.d4rk.androidtutorials.java.data.source;

import android.content.Intent;

/**
 * Local data access for the home feature.
 */
public interface HomeLocalDataSource {

    Intent getPlayStoreIntent();

    Intent getAppPlayStoreIntent(String packageName);

    String getDailyTip();
}
