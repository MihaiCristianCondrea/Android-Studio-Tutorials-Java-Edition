package com.d4rk.androidtutorials.java.ads.managers;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

/**
 * Helper class to load AdMob native ads into a container.
 */
public class NativeAdLoader {

    private static final String TAG = "NativeAdLoader";

    public static void load(@NonNull Context context, @NonNull ViewGroup container) {
        load(context, container, R.layout.ad_home_banner_large, new AdRequest.Builder().build(), null);
    }

    public static void load(@NonNull Context context, @NonNull ViewGroup container, @LayoutRes int layoutRes) {
        load(context, container, layoutRes, new AdRequest.Builder().build(), null);
    }

    public static void load(@NonNull Context context,
                            @NonNull ViewGroup container,
                            @LayoutRes int layoutRes,
                            @androidx.annotation.Nullable AdListener listener) {
        load(context, container, layoutRes, new AdRequest.Builder().build(), listener);
    }

    public static void load(@NonNull Context context,
                            @NonNull ViewGroup container,
                            @LayoutRes int layoutRes,
                            @NonNull AdRequest adRequest,
                            @androidx.annotation.Nullable AdListener listener) {
        AdLoader.Builder builder = new AdLoader.Builder(context, context.getString(R.string.native_ad_banner_unit_id))
                .forNativeAd(nativeAd -> {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    NativeAdView adView = (NativeAdView) inflater.inflate(layoutRes, container, false);
                    adView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    adView.setPadding(container.getPaddingLeft(), container.getPaddingTop(),
                            container.getPaddingRight(), container.getPaddingBottom());
                    container.setPadding(0, 0, 0, 0);
                    populateNativeAdView(nativeAd, adView);
                    container.removeAllViews();
                    container.addView(adView);
                    container.requestLayout();
                });

        builder.withAdListener(listener != null ? listener : new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                Log.w(TAG, "Failed to load native ad: " + loadAdError.getMessage());
                container.removeAllViews();
                container.setVisibility(View.GONE);
            }
        });

        AdLoader adLoader = builder.build();
        adLoader.loadAd(adRequest);
    }

    private static void populateNativeAdView(@NonNull NativeAd nativeAd, @NonNull NativeAdView adView) {
        MediaView mediaView = adView.findViewById(R.id.ad_media);
        TextView headlineView = adView.findViewById(R.id.ad_headline);
        TextView bodyView = adView.findViewById(R.id.ad_body);
        Button callToActionView = adView.findViewById(R.id.ad_call_to_action);
        ImageView iconView = adView.findViewById(R.id.ad_app_icon);
        TextView attributionView = adView.findViewById(R.id.ad_attribution);

        if (mediaView != null) {
            adView.setMediaView(mediaView);
        }
        adView.setHeadlineView(headlineView);
        adView.setBodyView(bodyView);
        adView.setCallToActionView(callToActionView);
        if (iconView != null) {
            adView.setIconView(iconView);
        }
        adView.setAdvertiserView(attributionView);

        if (headlineView != null) {
            headlineView.setText(nativeAd.getHeadline());
        }

        if (bodyView != null) {
            if (nativeAd.getBody() == null) {
                bodyView.setVisibility(View.GONE);
            } else {
                bodyView.setVisibility(View.VISIBLE);
                bodyView.setText(nativeAd.getBody());
            }
        }

        if (callToActionView != null) {
            if (nativeAd.getCallToAction() == null) {
                callToActionView.setVisibility(View.GONE);
            } else {
                callToActionView.setVisibility(View.VISIBLE);
                callToActionView.setText(nativeAd.getCallToAction());
            }
        }

        if (attributionView != null) {
            String adLabel = adView.getContext().getString(R.string.ad);
            if (nativeAd.getAdvertiser() == null) {
                attributionView.setText(adLabel);
            } else {
                attributionView.setText(adLabel + " " + nativeAd.getAdvertiser());
            }
        }

        if (iconView != null) {
            if (nativeAd.getIcon() == null) {
                iconView.setVisibility(View.GONE);
            } else {
                iconView.setImageDrawable(nativeAd.getIcon().getDrawable());
                iconView.setVisibility(View.VISIBLE);
            }
        }

        if (mediaView != null) {
            if (nativeAd.getMediaContent() == null) {
                mediaView.setVisibility(View.GONE);
            } else {
                mediaView.setMediaContent(nativeAd.getMediaContent());
                mediaView.setVisibility(View.VISIBLE);
            }
        }

        adView.setNativeAd(nativeAd);
    }
}
