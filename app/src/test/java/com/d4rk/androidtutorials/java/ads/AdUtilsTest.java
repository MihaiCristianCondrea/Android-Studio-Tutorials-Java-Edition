package com.d4rk.androidtutorials.java.ads;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import android.content.Context;
import android.test.mock.MockContext;
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

    private final TestContext context = new TestContext();

    @Before
    public void setUp() throws Exception {
        // Reset the initialized flag before each test
        Field field = AdUtils.class.getDeclaredField("initialized");
        field.setAccessible(true);
        field.set(null, false);
    }

    @Test
    public void initialize_callsMobileAdsInitializeOnlyOnce() {
        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.initialize(context);
            AdUtils.initialize(context);

            mobileAds.verify(() -> MobileAds.initialize(context), times(1));
        }
    }

    @Test
    public void loadBanner_withAdView_loadsAd() {
        FakeAdView adView = new FakeAdView(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(adView);
            mobileAds.verify(() -> MobileAds.initialize(context));
        }

        assertTrue(adView.isLoadAdCalled());
        assertNotNull(adView.getLastRequest());
    }

    @Test
    public void loadBanner_withNativeAdBannerView_loadsAd() {
        FakeNativeAdBannerView nativeView = new FakeNativeAdBannerView(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(nativeView);
            mobileAds.verify(() -> MobileAds.initialize(context));
        }

        assertTrue(nativeView.isLoadAdCalled());
    }

    @Test
    public void loadBanner_withOtherView_doesNothing() {
        View view = new View(context);

        try (MockedStatic<MobileAds> mobileAds = mockStatic(MobileAds.class)) {
            AdUtils.loadBanner(view);
            mobileAds.verifyNoInteractions();
        }

    }

    private static final class TestContext extends MockContext {
        @Override
        public Context getApplicationContext() {
            return this;
        }
    }

    private static final class FakeAdView extends AdView {
        private boolean loadAdCalled;
        private AdRequest lastRequest;

        FakeAdView(Context context) {
            super(context);
        }

        @Override
        public void loadAd(AdRequest adRequest) {
            loadAdCalled = true;
            lastRequest = adRequest;
        }

        boolean isLoadAdCalled() {
            return loadAdCalled;
        }

        AdRequest getLastRequest() {
            return lastRequest;
        }
    }

    private static final class FakeNativeAdBannerView extends NativeAdBannerView {
        private boolean loadAdCalled;

        FakeNativeAdBannerView(Context context) {
            super(context);
        }

        @Override
        public void loadAd() {
            loadAdCalled = true;
        }

        boolean isLoadAdCalled() {
            return loadAdCalled;
        }
    }
}
