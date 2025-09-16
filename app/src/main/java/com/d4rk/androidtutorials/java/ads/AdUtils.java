package com.d4rk.androidtutorials.java.ads;

import android.content.Context;
import android.view.View;

import com.d4rk.androidtutorials.java.ads.views.NativeAdBannerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.Objects;

public final class AdUtils {
    private static boolean initialized = false;

    private AdUtils() {
        // no-op
    }

    public static synchronized void initialize(Context context) {
        if (!initialized) {
            Context nonNullContext = Objects.requireNonNull(context, "context == null");
            Context appContext = nonNullContext.getApplicationContext();
            MobileAds.initialize(appContext != null ? appContext : nonNullContext);
            initialized = true;
        }
    }

    public static void loadBanner(View adView) {
        if (adView instanceof AdView view) {
            initialize(view.getContext());
            view.loadAd(new AdRequest.Builder().build());
        } else if (adView instanceof NativeAdBannerView nativeView) {
            initialize(nativeView.getContext());
            nativeView.loadAd();
        }
    }
}
