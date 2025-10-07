package com.d4rk.androidtutorials.java.ads.managers;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.viewbinding.ViewBinding;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.AdChoicesView;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Helper class to load AdMob native ads into a container.
 */
public class NativeAdLoader {

    private static final String TAG = "NativeAdLoader";

    public static void load(@NonNull Context context, @NonNull ViewGroup container) {
        load(context, container, R.layout.ad_home_banner_large, null, new AdRequest.Builder().build(), null);
    }

    public static void load(@NonNull Context context, @NonNull ViewGroup container, @LayoutRes int layoutRes) {
        load(context, container, layoutRes, null, new AdRequest.Builder().build(), null);
    }

    public static void load(@NonNull Context context,
                            @NonNull ViewGroup container,
                            @LayoutRes int layoutRes,
                            @androidx.annotation.Nullable AdListener listener) {
        load(context, container, layoutRes, null, new AdRequest.Builder().build(), listener);
    }

    public static void load(@NonNull Context context,
                            @NonNull ViewGroup container,
                            @LayoutRes int layoutRes,
                            @NonNull AdRequest adRequest,
                            @androidx.annotation.Nullable AdListener listener) {
        load(context, container, layoutRes, null, adRequest, listener);
    }

    public static void load(@NonNull Context context,
                            @NonNull ViewGroup container,
                            @LayoutRes int layoutRes,
                            @androidx.annotation.Nullable String adUnitId,
                            @NonNull AdRequest adRequest,
                            @androidx.annotation.Nullable AdListener listener) {
        String resolvedAdUnitId = TextUtils.isEmpty(adUnitId)
                ? context.getString(R.string.native_ad_fallback_unit_id)
                : adUnitId;

        AdLoader.Builder builder = new AdLoader.Builder(context, resolvedAdUnitId)
                .forNativeAd(nativeAd -> {
                    LayoutInflater inflater = LayoutInflater.from(context);
                    NativeAdView adView = (NativeAdView) inflater.inflate(layoutRes, container, false);
                    adView.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                    adView.setPadding(container.getPaddingLeft(), container.getPaddingTop(),
                            container.getPaddingRight(), container.getPaddingBottom());
                    container.setPadding(0, 0, 0, 0);
                    populateNativeAdView(nativeAd, adView, layoutRes);
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

    private static void populateNativeAdView(@NonNull NativeAd nativeAd,
                                             @NonNull NativeAdView adView,
                                             @LayoutRes int layoutRes) {
        ViewBinding binding = tryBind(adView, layoutRes);
        if (binding == null) {
            Log.w(TAG, "Could not bind native ad view for layout " + layoutRes);
            return;
        }

        MediaView mediaView = getBindingField(binding, "adMedia", MediaView.class);
        TextView headlineView = getBindingField(binding, "adHeadline", TextView.class);
        TextView bodyView = getBindingField(binding, "adBody", TextView.class);
        Button callToActionView = getBindingField(binding, "adCallToAction", Button.class);
        ImageView iconView = getBindingField(binding, "adAppIcon", ImageView.class);
        TextView attributionView = getBindingField(binding, "adAttribution", TextView.class);
        AdChoicesView adChoicesView = getBindingField(binding, "adChoices", AdChoicesView.class);

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
        if (adChoicesView != null) {
            adView.setAdChoicesView(adChoicesView);
        }

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

    @androidx.annotation.Nullable
    private static ViewBinding tryBind(@NonNull NativeAdView adView, @LayoutRes int layoutRes) {
        try {
            String resourceName = adView.getResources().getResourceEntryName(layoutRes);
            String bindingName = toBindingClassName(resourceName);
            String fullClassName = adView.getContext().getPackageName() + ".databinding." + bindingName;
            Class<?> bindingClass = Class.forName(fullClassName);
            Method bindMethod = bindingClass.getMethod("bind", View.class);
            return (ViewBinding) bindMethod.invoke(null, adView);
        } catch (Resources.NotFoundException | ClassNotFoundException | NoSuchMethodException |
                 IllegalAccessException | InvocationTargetException e) {
            Log.w(TAG, "Failed to create view binding for native ad layout", e);
            return null;
        }
    }

    @androidx.annotation.Nullable
    private static <T> T getBindingField(@NonNull ViewBinding binding,
                                         @NonNull String fieldName,
                                         @NonNull Class<T> type) {
        try {
            Field field = binding.getClass().getField(fieldName);
            Object value = field.get(binding);
            if (type.isInstance(value)) {
                return type.cast(value);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.w(TAG, "Unable to access binding field " + fieldName, e);
        }
        return null;
    }

    @NonNull
    private static String toBindingClassName(@NonNull String resourceName) {
        StringBuilder builder = new StringBuilder(resourceName.length());
        boolean capitalize = true;
        for (int i = 0; i < resourceName.length(); i++) {
            char c = resourceName.charAt(i);
            if (c == '_') {
                capitalize = true;
            } else {
                builder.append(capitalize ? Character.toUpperCase(c) : c);
                capitalize = false;
            }
        }
        builder.append("Binding");
        return builder.toString();
    }
}
