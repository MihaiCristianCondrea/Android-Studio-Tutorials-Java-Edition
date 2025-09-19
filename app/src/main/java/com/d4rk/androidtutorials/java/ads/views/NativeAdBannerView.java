package com.d4rk.androidtutorials.java.ads.views;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.managers.NativeAdLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

/**
 * Custom view that acts as a drop-in replacement for AdView and loads
 * a native ad using {@link NativeAdLoader} when {@link #loadAd()} is called.
 */
public class NativeAdBannerView extends FrameLayout {

    private int layoutRes = R.layout.ad_home_banner_large;

    public NativeAdBannerView(@NonNull Context context) {
        super(context);
        init(context, null, 0);
    }

    public NativeAdBannerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public NativeAdBannerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        LayoutTransition transition = new LayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
        transition.setAnimateParentHierarchy(false);
        setLayoutTransition(transition);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NativeAdBannerView, defStyleAttr, 0)) {
                layoutRes = a.getResourceId(R.styleable.NativeAdBannerView_nativeAdLayout, R.layout.ad_home_banner_large);
            }
        }
    }

    public void loadAd() {
        loadAd(new AdRequest.Builder().build(), null);
    }

    public void loadAd(@Nullable AdListener listener) {
        loadAd(new AdRequest.Builder().build(), listener);
    }

    public void loadAd(@NonNull AdRequest request) {
        loadAd(request, null);
    }

    public void loadAd(@NonNull AdRequest request, @Nullable AdListener listener) {
        NativeAdLoader.load(getContext(), this, layoutRes, request, listener);
    }

    public void setNativeAdLayout(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }
}
