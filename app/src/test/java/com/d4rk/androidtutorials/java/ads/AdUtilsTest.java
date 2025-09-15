package com.d4rk.androidtutorials.java.ads;

import static org.mockito.Mockito.*;

import android.content.Context;
import android.view.View;

import com.d4rk.androidtutorials.java.ads.views.NativeAdBannerView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;

/**
 * Unit tests for {@link AdUtils}.
 */
public class AdUtilsTest {

    @Before
    public void setUp() throws Exception {
        // Reset the initialized flag before each test
        Field field = AdUtils.class.getDeclaredField("initialized");
        field.setAccessible(true);
        field.set(null, false);
    }

    @Test
    public void initialize_callsMobileAdsInitializeOnlyOnce() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.initialize(context);
            AdUtils.initialize(context);

            mobileAds.verify(() -> MobileAds.initialize(context), times(1));
        }
    }

    @Test
    public void loadBanner_withAdView_loadsAd() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        AdView adView = mock(AdView.class);
        when(adView.getContext()).thenReturn(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(adView);
            mobileAds.verify(() -> MobileAds.initialize(context));
        }

        verify(adView, times(1)).loadAd(any(AdRequest.class));
    }

    @Test
    public void loadBanner_withAdView_multipleCalls_initializeOnlyOnce() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        AdView adView = mock(AdView.class);
        when(adView.getContext()).thenReturn(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(adView);
            AdUtils.loadBanner(adView);

            mobileAds.verify(() -> MobileAds.initialize(context), times(1));
        }

        verify(adView, times(2)).loadAd(any(AdRequest.class));
    }

    @Test
    public void loadBanner_withNativeAdBannerView_loadsAd() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        NativeAdBannerView nativeView = mock(NativeAdBannerView.class);
        when(nativeView.getContext()).thenReturn(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(nativeView);
            mobileAds.verify(() -> MobileAds.initialize(context));
        }

        verify(nativeView, times(1)).loadAd();
    }

    @Test
    public void loadBanner_withOtherView_doesNothing() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        View view = mock(View.class);
        when(view.getContext()).thenReturn(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(view);
            mobileAds.verifyNoInteractions();
        }
    }
}
