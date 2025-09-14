package com.d4rk.androidtutorials.java.ads.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.test.core.app.ApplicationProvider;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedConstruction;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.atomic.AtomicReference;

@RunWith(RobolectricTestRunner.class)
public class NativeAdLoaderTest {

    @Test
    public void loadSuccessAddsPopulatedViewAndRequestsLayout() {
        Context context = ApplicationProvider.getApplicationContext();
        TestContainer container = new TestContainer(context);
        AdRequest adRequest = mock(AdRequest.class);

        NativeAd nativeAd = mock(NativeAd.class);
        when(nativeAd.getHeadline()).thenReturn("Headline");
        when(nativeAd.getBody()).thenReturn("Body");
        when(nativeAd.getCallToAction()).thenReturn("Install");
        when(nativeAd.getAdvertiser()).thenReturn("Advertiser");

        AtomicReference<NativeAd.OnNativeAdLoadedListener> listenerRef = new AtomicReference<>();

        try (MockedConstruction<AdLoader.Builder> mocked = mockConstruction(AdLoader.Builder.class, (builder, ctx) -> {
            when(builder.forNativeAd(any())).thenAnswer(inv -> {
                listenerRef.set(inv.getArgument(0));
                return builder;
            });
            when(builder.withAdListener(any())).thenReturn(builder);
            AdLoader adLoader = mock(AdLoader.class);
            when(builder.build()).thenReturn(adLoader);
            doAnswer(inv -> {
                listenerRef.get().onNativeAdLoaded(nativeAd);
                return null;
            }).when(adLoader).loadAd(any(AdRequest.class));
        })) {
            NativeAdLoader.load(context, container, R.layout.ad_home_banner_large, adRequest, null);
        }

        assertEquals(1, container.getChildCount());
        assertTrue(container.requestedLayout);
        NativeAdView adView = (NativeAdView) container.getChildAt(0);
        TextView headline = adView.findViewById(R.id.ad_headline);
        assertEquals("Headline", headline.getText().toString());
        Button cta = adView.findViewById(R.id.ad_call_to_action);
        assertEquals("Install", cta.getText().toString());
    }

    @Test
    public void loadFailureClearsContainerAndHides() {
        Context context = ApplicationProvider.getApplicationContext();
        TestContainer container = new TestContainer(context);
        container.addView(new View(context));
        AdRequest adRequest = mock(AdRequest.class);

        AtomicReference<AdListener> listenerRef = new AtomicReference<>();

        try (MockedConstruction<AdLoader.Builder> mocked = mockConstruction(AdLoader.Builder.class, (builder, ctx) -> {
            when(builder.forNativeAd(any())).thenReturn(builder);
            when(builder.withAdListener(any())).thenAnswer(inv -> {
                listenerRef.set(inv.getArgument(0));
                return builder;
            });
            AdLoader adLoader = mock(AdLoader.class);
            when(builder.build()).thenReturn(adLoader);
            doAnswer(inv -> {
                listenerRef.get().onAdFailedToLoad(mock(LoadAdError.class));
                return null;
            }).when(adLoader).loadAd(any(AdRequest.class));
        })) {
            NativeAdLoader.load(context, container, R.layout.ad_home_banner_large, adRequest, null);
        }

        assertEquals(0, container.getChildCount());
        assertEquals(View.GONE, container.getVisibility());
    }

    private static class TestContainer extends FrameLayout {
        boolean requestedLayout = false;

        TestContainer(Context context) {
            super(context);
        }

        @Override
        public void requestLayout() {
            super.requestLayout();
            requestedLayout = true;
        }
    }
}

