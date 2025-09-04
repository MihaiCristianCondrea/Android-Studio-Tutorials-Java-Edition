package com.d4rk.androidtutorials.java.ads.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.AdRequest;
import com.d4rk.androidtutorials.java.ads.managers.NativeAdLoader;

/**
 * Custom view that acts as a drop-in replacement for AdView and loads
 * a native ad using {@link NativeAdLoader} when {@link #loadAd(AdRequest)} is called.
 */
public class NativeAdBannerView extends FrameLayout {

    public NativeAdBannerView(@NonNull Context context) {
        super(context);
    }

    public NativeAdBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NativeAdBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadAd(AdRequest adRequest) {
        NativeAdLoader.load(getContext(), this);
    }
}
