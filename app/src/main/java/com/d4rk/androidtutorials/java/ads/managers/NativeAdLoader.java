package com.d4rk.androidtutorials.java.ads.managers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

/**
 * Helper class to load AdMob native ads into a container.
 */
public class NativeAdLoader {

    public static void load(@NonNull Context context, @NonNull ViewGroup container) {
        AdLoader adLoader = new AdLoader.Builder(context, context.getString(R.string.ad_banner_unit_id))
                .forNativeAd(nativeAd -> {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    NativeAdView adView = (NativeAdView) inflater.inflate(R.layout.native_ad, container, false);
                    populateNativeAdView(nativeAd, adView);
                    container.removeAllViews();
                    container.addView(adView);
                })
                .build();
        adLoader.loadAd(new AdRequest.Builder().build());
    }

    private static void populateNativeAdView(@NonNull NativeAd nativeAd, @NonNull NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));

        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.GONE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.GONE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);
    }
}
