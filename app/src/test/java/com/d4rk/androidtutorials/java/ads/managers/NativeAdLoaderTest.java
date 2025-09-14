package com.d4rk.androidtutorials.java.ads.managers;

import static org.mockito.Mockito.*;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Tests for {@link NativeAdLoader}.
 */
public class NativeAdLoaderTest {

    @Test
    public void populateNativeAdViewPopulatesViews() throws Exception {
        NativeAd nativeAd = mock(NativeAd.class);
        when(nativeAd.getHeadline()).thenReturn("headline");
        when(nativeAd.getBody()).thenReturn("body");
        when(nativeAd.getCallToAction()).thenReturn("cta");
        NativeAd.Image icon = mock(NativeAd.Image.class);
        Drawable drawable = mock(Drawable.class);
        when(icon.getDrawable()).thenReturn(drawable);
        when(nativeAd.getIcon()).thenReturn(icon);
        when(nativeAd.getAdvertiser()).thenReturn("advertiser");
        NativeAd.MediaContent mediaContent = mock(NativeAd.MediaContent.class);
        when(nativeAd.getMediaContent()).thenReturn(mediaContent);

        NativeAdView adView = mock(NativeAdView.class);
        MediaView mediaView = mock(MediaView.class);
        TextView headlineView = mock(TextView.class);
        TextView bodyView = mock(TextView.class);
        Button callToActionView = mock(Button.class);
        ImageView iconView = mock(ImageView.class);
        TextView attributionView = mock(TextView.class);

        when(adView.findViewById(R.id.ad_media)).thenReturn(mediaView);
        when(adView.findViewById(R.id.ad_headline)).thenReturn(headlineView);
        when(adView.findViewById(R.id.ad_body)).thenReturn(bodyView);
        when(adView.findViewById(R.id.ad_call_to_action)).thenReturn(callToActionView);
        when(adView.findViewById(R.id.ad_app_icon)).thenReturn(iconView);
        when(adView.findViewById(R.id.ad_attribution)).thenReturn(attributionView);
        Context context = mock(Context.class);
        when(adView.getContext()).thenReturn(context);
        when(context.getString(R.string.ad)).thenReturn("Ad");

        Method method = NativeAdLoader.class.getDeclaredMethod("populateNativeAdView", NativeAd.class, NativeAdView.class);
        method.setAccessible(true);
        method.invoke(null, nativeAd, adView);

        verify(headlineView).setText("headline");
        verify(bodyView).setVisibility(View.VISIBLE);
        verify(bodyView).setText("body");
        verify(callToActionView).setVisibility(View.VISIBLE);
        verify(callToActionView).setText("cta");
        verify(attributionView).setText("Ad advertiser");
        verify(iconView).setImageDrawable(drawable);
        verify(iconView).setVisibility(View.VISIBLE);
        verify(mediaView).setMediaContent(mediaContent);
        verify(mediaView).setVisibility(View.VISIBLE);
        verify(adView).setNativeAd(nativeAd);
    }

    @Test
    public void populateNativeAdViewHidesMissingViews() throws Exception {
        NativeAd nativeAd = mock(NativeAd.class);
        when(nativeAd.getHeadline()).thenReturn("headline");
        when(nativeAd.getBody()).thenReturn(null);
        when(nativeAd.getCallToAction()).thenReturn(null);
        when(nativeAd.getIcon()).thenReturn(null);
        when(nativeAd.getAdvertiser()).thenReturn(null);
        when(nativeAd.getMediaContent()).thenReturn(null);

        NativeAdView adView = mock(NativeAdView.class);
        MediaView mediaView = mock(MediaView.class);
        TextView headlineView = mock(TextView.class);
        TextView bodyView = mock(TextView.class);
        Button callToActionView = mock(Button.class);
        ImageView iconView = mock(ImageView.class);
        TextView attributionView = mock(TextView.class);

        when(adView.findViewById(R.id.ad_media)).thenReturn(mediaView);
        when(adView.findViewById(R.id.ad_headline)).thenReturn(headlineView);
        when(adView.findViewById(R.id.ad_body)).thenReturn(bodyView);
        when(adView.findViewById(R.id.ad_call_to_action)).thenReturn(callToActionView);
        when(adView.findViewById(R.id.ad_app_icon)).thenReturn(iconView);
        when(adView.findViewById(R.id.ad_attribution)).thenReturn(attributionView);
        Context context = mock(Context.class);
        when(adView.getContext()).thenReturn(context);
        when(context.getString(R.string.ad)).thenReturn("Ad");

        Method method = NativeAdLoader.class.getDeclaredMethod("populateNativeAdView", NativeAd.class, NativeAdView.class);
        method.setAccessible(true);
        method.invoke(null, nativeAd, adView);

        verify(bodyView).setVisibility(View.GONE);
        verify(callToActionView).setVisibility(View.GONE);
        verify(attributionView).setText("Ad");
        verify(iconView).setVisibility(View.GONE);
        verify(mediaView).setVisibility(View.GONE);
    }

    @Test
    public void onAdFailedToLoadClearsContainer() throws Exception {
        ViewGroup container = mock(ViewGroup.class);

        Class<?> clazz = Class.forName("com.d4rk.androidtutorials.java.ads.managers.NativeAdLoader$1");
        Constructor<?> ctor = clazz.getDeclaredConstructor(ViewGroup.class);
        ctor.setAccessible(true);
        AdListener listener = (AdListener) ctor.newInstance(container);

        LoadAdError loadAdError = mock(LoadAdError.class);
        listener.onAdFailedToLoad(loadAdError);

        verify(container).removeAllViews();
        verify(container).setVisibility(View.GONE);
    }
}

