package com.d4rk.androidtutorials.java.ads.preferences;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.views.NativeAdBannerView;
import com.google.android.gms.ads.AdRequest;

/**
 * Preference that displays a NativeAdBannerView.
 */
public class NativeAdPreference extends Preference {

    public NativeAdPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setLayoutResource(R.layout.native_ad_preference);
        setSelectable(false);
    }

    public NativeAdPreference(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NativeAdPreference(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NativeAdPreference(@NonNull Context context) {
        this(context, null);
    }

    @Override
    public void onBindViewHolder(@NonNull PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);
        NativeAdBannerView adView = (NativeAdBannerView) holder.itemView.findViewById(R.id.native_ad_view);
        if (adView != null) {
            adView.loadAd(new AdRequest.Builder().build());
        }
    }
}
