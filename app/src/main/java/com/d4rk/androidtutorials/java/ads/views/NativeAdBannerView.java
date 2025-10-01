package com.d4rk.androidtutorials.java.ads.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

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
    @Nullable
    private String adUnitId;

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
        adUnitId = context.getString(R.string.native_ad_fallback_unit_id);

        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NativeAdBannerView, defStyleAttr, 0)) {
                layoutRes = a.getResourceId(R.styleable.NativeAdBannerView_nativeAdLayout, R.layout.ad_home_banner_large);
                CharSequence adUnitValue = a.getText(R.styleable.NativeAdBannerView_nativeAdUnitId);
                if (!TextUtils.isEmpty(adUnitValue)) {
                    adUnitId = adUnitValue.toString();
                }
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
        NativeAdLoader.load(getContext(), this, layoutRes, adUnitId, request, listener);
    }

    public void setNativeAdLayout(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public void setNativeAdUnitId(@Nullable String adUnitId) {
        if (TextUtils.isEmpty(adUnitId)) {
            this.adUnitId = getContext().getString(R.string.native_ad_fallback_unit_id);
        } else {
            this.adUnitId = adUnitId;
        }
    }

    public void setNativeAdUnitId(@StringRes int adUnitIdRes) {
        setNativeAdUnitId(getContext().getString(adUnitIdRes));
    }
}
