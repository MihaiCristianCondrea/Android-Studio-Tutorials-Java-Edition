package com.d4rk.androidtutorials.java.ads.views;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;

import android.content.Context;
import android.util.AttributeSet;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.managers.NativeAdLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

@RunWith(RobolectricTestRunner.class)
public class NativeAdBannerViewTest {

    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
    }

    @Test
    public void loadAd_withoutArguments_delegatesToNativeAdLoader() {
        NativeAdBannerView view = new NativeAdBannerView(context);

        try (MockedStatic<NativeAdLoader> loaderMock = Mockito.mockStatic(NativeAdLoader.class)) {
            view.loadAd();

            loaderMock.verify(() -> NativeAdLoader.load(
                    eq(context),
                    eq(view),
                    eq(R.layout.ad_home_banner_large),
                    any(AdRequest.class),
                    isNull()
            ));
            loaderMock.verifyNoMoreInteractions();
        }
    }

    @Test
    public void loadAd_withListener_delegatesToNativeAdLoader() {
        NativeAdBannerView view = new NativeAdBannerView(context);
        AdListener listener = new AdListener() { };

        try (MockedStatic<NativeAdLoader> loaderMock = Mockito.mockStatic(NativeAdLoader.class)) {
            view.loadAd(listener);

            loaderMock.verify(() -> NativeAdLoader.load(
                    eq(context),
                    eq(view),
                    eq(R.layout.ad_home_banner_large),
                    any(AdRequest.class),
                    eq(listener)
            ));
            loaderMock.verifyNoMoreInteractions();
        }
    }

    @Test
    public void loadAd_withRequest_delegatesToNativeAdLoader() {
        NativeAdBannerView view = new NativeAdBannerView(context);
        AdRequest request = new AdRequest.Builder().build();

        try (MockedStatic<NativeAdLoader> loaderMock = Mockito.mockStatic(NativeAdLoader.class)) {
            view.loadAd(request);

            loaderMock.verify(() -> NativeAdLoader.load(
                    eq(context),
                    eq(view),
                    eq(R.layout.ad_home_banner_large),
                    eq(request),
                    isNull()
            ));
            loaderMock.verifyNoMoreInteractions();
        }
    }

    @Test
    public void loadAd_withRequestAndListener_delegatesToNativeAdLoader() {
        NativeAdBannerView view = new NativeAdBannerView(context);
        AdRequest request = new AdRequest.Builder().build();
        AdListener listener = new AdListener() { };

        try (MockedStatic<NativeAdLoader> loaderMock = Mockito.mockStatic(NativeAdLoader.class)) {
            view.loadAd(request, listener);

            loaderMock.verify(() -> NativeAdLoader.load(
                    eq(context),
                    eq(view),
                    eq(R.layout.ad_home_banner_large),
                    eq(request),
                    eq(listener)
            ));
            loaderMock.verifyNoMoreInteractions();
        }
    }

    @Test
    public void constructor_withNativeAdLayoutAttribute_setsLayoutResource() {
        AttributeSet attrs = Robolectric.buildAttributeSet()
                .addAttribute(R.attr.nativeAdLayout, "@layout/ad_home_banner_small")
                .build();
        NativeAdBannerView view = new NativeAdBannerView(context, attrs);

        try (MockedStatic<NativeAdLoader> loaderMock = Mockito.mockStatic(NativeAdLoader.class)) {
            view.loadAd();

            loaderMock.verify(() -> NativeAdLoader.load(
                    eq(context),
                    eq(view),
                    eq(R.layout.ad_home_banner_small),
                    any(AdRequest.class),
                    isNull()
            ));
            loaderMock.verifyNoMoreInteractions();
        }
    }
}
