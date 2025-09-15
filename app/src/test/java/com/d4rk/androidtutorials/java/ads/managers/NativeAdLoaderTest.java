package com.d4rk.androidtutorials.java.ads.managers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Method;

public class NativeAdLoaderTest {

    private Method populateMethod;

    @Before
    public void setUp() throws Exception {
        populateMethod = NativeAdLoader.class
                .getDeclaredMethod("populateNativeAdView", NativeAd.class, NativeAdView.class);
        populateMethod.setAccessible(true);
    }

    @Test
    public void populateNativeAdView_withAllAssets_setsContentAndVisibility() throws Exception {
        NativeAd nativeAd = mock(NativeAd.class);
        MediaView mediaView = mock(MediaView.class);
        TextView headlineView = mock(TextView.class);
        TextView bodyView = mock(TextView.class);
        Button callToActionView = mock(Button.class);
        ImageView iconView = mock(ImageView.class);
        TextView attributionView = mock(TextView.class);
        Context context = mock(Context.class);
        NativeAdView adView = prepareAdView(mediaView, headlineView, bodyView, callToActionView, iconView,
                attributionView, context);

        when(context.getString(R.string.ad)).thenReturn("Ad");
        when(nativeAd.getHeadline()).thenReturn("Headline");
        when(nativeAd.getBody()).thenReturn("Body");
        when(nativeAd.getCallToAction()).thenReturn("Install");
        when(nativeAd.getAdvertiser()).thenReturn("Advertiser");
        NativeAd.Image icon = mock(NativeAd.Image.class);
        Drawable drawable = mock(Drawable.class);
        when(icon.getDrawable()).thenReturn(drawable);
        when(nativeAd.getIcon()).thenReturn(icon);
        NativeAd.MediaContent mediaContent = mock(NativeAd.MediaContent.class);
        when(nativeAd.getMediaContent()).thenReturn(mediaContent);

        invokePopulate(nativeAd, adView);

        verify(headlineView).setText("Headline");
        verify(bodyView).setVisibility(View.VISIBLE);
        verify(bodyView).setText("Body");
        verify(callToActionView).setVisibility(View.VISIBLE);
        verify(callToActionView).setText("Install");
        verify(attributionView).setText("Ad Advertiser");
        verify(iconView).setImageDrawable(drawable);
        verify(iconView).setVisibility(View.VISIBLE);
        verify(mediaView).setMediaContent(mediaContent);
        verify(mediaView).setVisibility(View.VISIBLE);
        verify(adView).setNativeAd(nativeAd);
    }

    @Test
    public void populateNativeAdView_withMissingAssets_hidesViewsOrSetsDefaults() throws Exception {
        NativeAd nativeAd = mock(NativeAd.class);
        MediaView mediaView = mock(MediaView.class);
        TextView headlineView = mock(TextView.class);
        TextView bodyView = mock(TextView.class);
        Button callToActionView = mock(Button.class);
        ImageView iconView = mock(ImageView.class);
        TextView attributionView = mock(TextView.class);
        Context context = mock(Context.class);
        NativeAdView adView = prepareAdView(mediaView, headlineView, bodyView, callToActionView, iconView,
                attributionView, context);

        when(context.getString(R.string.ad)).thenReturn("Ad");
        when(nativeAd.getHeadline()).thenReturn(null);
        when(nativeAd.getBody()).thenReturn(null);
        when(nativeAd.getCallToAction()).thenReturn(null);
        when(nativeAd.getAdvertiser()).thenReturn(null);
        when(nativeAd.getIcon()).thenReturn(null);
        when(nativeAd.getMediaContent()).thenReturn(null);

        invokePopulate(nativeAd, adView);

        verify(headlineView).setText(isNull());
        verify(bodyView).setVisibility(View.GONE);
        verify(bodyView, never()).setText(any(CharSequence.class));
        verify(callToActionView).setVisibility(View.GONE);
        verify(callToActionView, never()).setText(any(CharSequence.class));
        verify(attributionView).setText("Ad");
        verify(iconView).setVisibility(View.GONE);
        verify(iconView, never()).setImageDrawable(any(Drawable.class));
        verify(mediaView).setVisibility(View.GONE);
        verify(mediaView, never()).setMediaContent(any(NativeAd.MediaContent.class));
        verify(adView).setNativeAd(nativeAd);
    }

    private NativeAdView prepareAdView(MediaView mediaView,
                                       TextView headlineView,
                                       TextView bodyView,
                                       Button callToActionView,
                                       ImageView iconView,
                                       TextView attributionView,
                                       Context context) {
        NativeAdView adView = mock(NativeAdView.class);
        when(adView.findViewById(R.id.ad_media)).thenReturn(mediaView);
        when(adView.findViewById(R.id.ad_headline)).thenReturn(headlineView);
        when(adView.findViewById(R.id.ad_body)).thenReturn(bodyView);
        when(adView.findViewById(R.id.ad_call_to_action)).thenReturn(callToActionView);
        when(adView.findViewById(R.id.ad_app_icon)).thenReturn(iconView);
        when(adView.findViewById(R.id.ad_attribution)).thenReturn(attributionView);
        when(adView.getContext()).thenReturn(context);
        return adView;
    }

    private void invokePopulate(NativeAd nativeAd, NativeAdView adView) throws Exception {
        populateMethod.invoke(null, nativeAd, adView);
    }
}
