package com.d4rk.androidtutorials.java.ads;

import android.content.Context;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public final class AdUtils {
    private static boolean initialized = false;

    private AdUtils() {
        // no-op
    }

    public static synchronized void initialize(Context context) {
        if (!initialized) {
            MobileAds.initialize(context.getApplicationContext());
            initialized = true;
        }
    }

    public static void loadBanner(View adView) {
        if (adView instanceof AdView) {
            AdView view = (AdView) adView;
            initialize(view.getContext());
            view.loadAd(new AdRequest.Builder().build());
        }
    }
}
