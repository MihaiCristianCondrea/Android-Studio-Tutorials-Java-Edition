package com.d4rk.androidtutorials.java.ads;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.managers.NativeAdLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAd.OnNativeAdLoadedListener;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests for {@link NativeAdLoader}.
 */
public class NativeAdLoaderTest {

    @Test
    public void load_whenNativeAdLoaded_populatesContainerAndLoadsAd() throws Exception {
        Context context = mock(Context.class);
        when(context.getString(R.string.native_ad_banner_unit_id)).thenReturn("unit-id");
        when(context.getString(R.string.ad)).thenReturn("Ad");

        ViewGroup container = mock(ViewGroup.class);
        when(container.getPaddingLeft()).thenReturn(4);
        when(container.getPaddingTop()).thenReturn(5);
        when(container.getPaddingRight()).thenReturn(6);
        when(container.getPaddingBottom()).thenReturn(7);

        LayoutInflater inflater = mock(LayoutInflater.class);
        NativeAdView adView = mock(NativeAdView.class);
        when(adView.getContext()).thenReturn(context);
        when(inflater.inflate(eq(R.layout.ad_home_banner_large), eq(container), eq(false))).thenReturn(adView);

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

        NativeAd nativeAd = mock(NativeAd.class);
        when(nativeAd.getHeadline()).thenReturn("Headline");
        when(nativeAd.getBody()).thenReturn("Body");
        when(nativeAd.getCallToAction()).thenReturn("Install");
        when(nativeAd.getAdvertiser()).thenReturn("Advertiser");

        NativeAd.Image iconAsset = mock(NativeAd.Image.class);
        Drawable iconDrawable = mock(Drawable.class);
        when(iconAsset.getDrawable()).thenReturn(iconDrawable);
        when(nativeAd.getIcon()).thenReturn(iconAsset);

        NativeAd.MediaContent mediaContent = mock(NativeAd.MediaContent.class);
        when(nativeAd.getMediaContent()).thenReturn(mediaContent);

        AdRequest adRequest = mock(AdRequest.class);
        AdListener listener = mock(AdListener.class);
        AdLoader adLoader = mock(AdLoader.class);

        AtomicReference<OnNativeAdLoadedListener> adLoadedListener = new AtomicReference<>();
        AtomicReference<List<?>> builderArgs = new AtomicReference<>();

        try (MockedStatic<LayoutInflater> inflaterStatic = mockStatic(LayoutInflater.class);
             MockedConstruction<AdLoader.Builder> builderConstruction =
                     mockConstruction(AdLoader.Builder.class, (mockBuilder, constructionContext) -> {
                         builderArgs.set(constructionContext.arguments());
                         doAnswer(invocation -> {
                             adLoadedListener.set(invocation.getArgument(0));
                             return mockBuilder;
                         }).when(mockBuilder).forNativeAd(any(OnNativeAdLoadedListener.class));
                         when(mockBuilder.withAdListener(any(AdListener.class))).thenReturn(mockBuilder);
                         when(mockBuilder.build()).thenReturn(adLoader);
                     })) {

            inflaterStatic.when(() -> LayoutInflater.from(context)).thenReturn(inflater);

            NativeAdLoader.load(context, container, R.layout.ad_home_banner_large, adRequest, listener);

            verify(adLoader, times(1)).loadAd(adRequest);

            OnNativeAdLoadedListener capturedListener = adLoadedListener.get();
            assertNotNull(capturedListener);
            capturedListener.onNativeAdLoaded(nativeAd);

            AdLoader.Builder builderMock = builderConstruction.constructed().get(0);
            verify(builderMock, times(1)).forNativeAd(any(OnNativeAdLoadedListener.class));
            verify(builderMock, times(1)).withAdListener(listener);
            verify(builderMock, times(1)).build();

            verify(container, times(1)).removeAllViews();
            verify(container, times(1)).addView(adView);
            verify(container, times(1)).requestLayout();
            verify(container, times(1)).setPadding(0, 0, 0, 0);

            verify(adView, times(1)).setLayoutParams(any(ViewGroup.LayoutParams.class));
            verify(adView, times(1)).setPadding(4, 5, 6, 7);
            verify(adView, times(1)).setMediaView(mediaView);
            verify(adView, times(1)).setHeadlineView(headlineView);
            verify(adView, times(1)).setBodyView(bodyView);
            verify(adView, times(1)).setCallToActionView(callToActionView);
            verify(adView, times(1)).setIconView(iconView);
            verify(adView, times(1)).setAdvertiserView(attributionView);

            verify(headlineView, times(1)).setText("Headline");
            verify(bodyView, times(1)).setVisibility(View.VISIBLE);
            verify(bodyView, times(1)).setText("Body");
            verify(callToActionView, times(1)).setVisibility(View.VISIBLE);
            verify(callToActionView, times(1)).setText("Install");
            verify(attributionView, times(1)).setText("Ad Advertiser");
            verify(iconView, times(1)).setVisibility(View.VISIBLE);
            verify(iconView, times(1)).setImageDrawable(iconDrawable);
            verify(mediaView, times(1)).setVisibility(View.VISIBLE);
            verify(mediaView, times(1)).setMediaContent(mediaContent);
            verify(adView, times(1)).setNativeAd(nativeAd);
        }

        assertNotNull(builderArgs.get());
        assertEquals(context, builderArgs.get().get(0));
        assertEquals("unit-id", builderArgs.get().get(1));
    }

    @Test
    public void populateNativeAdView_handlesMissingOptionalAssets() throws Exception {
        NativeAd nativeAd = mock(NativeAd.class);
        when(nativeAd.getHeadline()).thenReturn("Headline");
        when(nativeAd.getBody()).thenReturn(null);
        when(nativeAd.getCallToAction()).thenReturn(null);
        when(nativeAd.getAdvertiser()).thenReturn(null);
        when(nativeAd.getIcon()).thenReturn(null);
        when(nativeAd.getMediaContent()).thenReturn(null);

        NativeAdView adView = mock(NativeAdView.class);
        Context context = mock(Context.class);
        when(adView.getContext()).thenReturn(context);
        when(context.getString(R.string.ad)).thenReturn("Ad");

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

        invokePopulateNativeAdView(nativeAd, adView);

        verify(adView, times(1)).setMediaView(mediaView);
        verify(adView, times(1)).setHeadlineView(headlineView);
        verify(adView, times(1)).setBodyView(bodyView);
        verify(adView, times(1)).setCallToActionView(callToActionView);
        verify(adView, times(1)).setIconView(iconView);
        verify(adView, times(1)).setAdvertiserView(attributionView);

        verify(headlineView, times(1)).setText("Headline");
        verify(bodyView, times(1)).setVisibility(View.GONE);
        verify(bodyView, never()).setText(anyString());
        verify(callToActionView, times(1)).setVisibility(View.GONE);
        verify(callToActionView, never()).setText(anyString());
        verify(attributionView, times(1)).setText("Ad");
        verify(iconView, times(1)).setVisibility(View.GONE);
        verify(iconView, never()).setImageDrawable(any(Drawable.class));
        verify(mediaView, times(1)).setVisibility(View.GONE);
        verify(mediaView, never()).setMediaContent(any());
        verify(adView, times(1)).setNativeAd(nativeAd);
    }

    private static void invokePopulateNativeAdView(NativeAd nativeAd, NativeAdView adView) throws Exception {
        Method method = NativeAdLoader.class.getDeclaredMethod("populateNativeAdView", NativeAd.class, NativeAdView.class);
        method.setAccessible(true);
        method.invoke(null, nativeAd, adView);
    }
}
