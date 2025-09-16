package com.d4rk.androidtutorials.java.ads.managers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Unit tests for {@link NativeAdLoader}.
 */
public class NativeAdLoaderTest {

    private TestMainThreadExecutor mainThreadExecutor;

    @Before
    public void setUp() {
        mainThreadExecutor = new TestMainThreadExecutor();
        NativeAdLoader.setMainThreadExecutorForTesting(mainThreadExecutor);
    }

    @After
    public void tearDown() {
        NativeAdLoader.resetMainThreadExecutorForTesting();
    }

    @Test
    public void load_whenNativeAdLoaded_postsViewPopulationAndNotifiesListener() {
        Context context = mock(Context.class);
        when(context.getString(R.string.native_ad_banner_unit_id)).thenReturn("ad-unit");
        when(context.getString(R.string.ad)).thenReturn("Ad");

        ViewGroup container = mock(ViewGroup.class);
        AdListener externalListener = mock(AdListener.class);
        AdRequest adRequest = mock(AdRequest.class);

        NativeAd nativeAd = mock(NativeAd.class);
        when(nativeAd.getHeadline()).thenReturn("Headline");
        when(nativeAd.getBody()).thenReturn("Body");
        when(nativeAd.getCallToAction()).thenReturn("Install");

        NativeAd.Image icon = mock(NativeAd.Image.class);
        Drawable drawable = mock(Drawable.class);
        when(icon.getDrawable()).thenReturn(drawable);
        when(nativeAd.getIcon()).thenReturn(icon);

        LayoutInflater layoutInflater = mock(LayoutInflater.class);
        NativeAdView adView = mock(NativeAdView.class);
        when(adView.getContext()).thenReturn(context);

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

        AdLoader adLoader = mock(AdLoader.class);
        AtomicReference<NativeAd.OnNativeAdLoadedListener> nativeAdListenerRef = new AtomicReference<>();
        AtomicReference<AdListener> adListenerRef = new AtomicReference<>();

        try (MockedStatic<LayoutInflater> layoutInflaterStatic = org.mockito.Mockito.mockStatic(LayoutInflater.class);
             MockedConstruction<AdLoader.Builder> mockedBuilder = org.mockito.Mockito.mockConstruction(
                     AdLoader.Builder.class,
                     (mockBuilder, contextArgs) -> {
                         when(mockBuilder.forNativeAd(any())).thenAnswer(invocation -> {
                             nativeAdListenerRef.set(invocation.getArgument(0));
                             return mockBuilder;
                         });
                         when(mockBuilder.withAdListener(any())).thenAnswer(invocation -> {
                             adListenerRef.set(invocation.getArgument(0));
                             return mockBuilder;
                         });
                         when(mockBuilder.build()).thenReturn(adLoader);
                     })) {

            layoutInflaterStatic.when(() -> LayoutInflater.from(context)).thenReturn(layoutInflater);
            when(layoutInflater.inflate(anyInt(), eq(container), eq(false))).thenReturn(adView);

            NativeAdLoader.load(context, container, R.layout.ad_home_banner_large, adRequest, externalListener);

            NativeAd.OnNativeAdLoadedListener nativeAdListener = nativeAdListenerRef.get();
            AdListener adListener = adListenerRef.get();
            assertNotNull(nativeAdListener);
            assertNotNull(adListener);

            nativeAdListener.onNativeAdLoaded(nativeAd);
            assertTrue(mainThreadExecutor.hasPendingTasks());
            verify(container, never()).addView(any(View.class));

            adListener.onAdLoaded();
            assertTrue(mainThreadExecutor.hasPendingTasks());
            verify(externalListener, never()).onAdLoaded();

            mainThreadExecutor.drain();

            verify(layoutInflater).inflate(R.layout.ad_home_banner_large, container, false);
            verify(container).removeAllViews();
            verify(container).addView(adView);
            verify(container).requestLayout();
            verify(container).setVisibility(View.VISIBLE);
            verify(adView).setNativeAd(nativeAd);
            verify(externalListener, times(1)).onAdLoaded();
            verify(adLoader, times(1)).loadAd(adRequest);
            assertFalse(mainThreadExecutor.hasPendingTasks());
        }
    }

    @Test
    public void load_whenAdFails_postsFailureAndNotifiesListener() {
        Context context = mock(Context.class);
        when(context.getString(R.string.native_ad_banner_unit_id)).thenReturn("ad-unit");

        ViewGroup container = mock(ViewGroup.class);
        AdListener externalListener = mock(AdListener.class);
        AdRequest adRequest = mock(AdRequest.class);
        LoadAdError loadAdError = mock(LoadAdError.class);

        AtomicReference<AdListener> adListenerRef = new AtomicReference<>();

        try (MockedConstruction<AdLoader.Builder> mockedBuilder = org.mockito.Mockito.mockConstruction(
                AdLoader.Builder.class,
                (mockBuilder, contextArgs) -> {
                    when(mockBuilder.forNativeAd(any())).thenReturn(mockBuilder);
                    when(mockBuilder.withAdListener(any())).thenAnswer(invocation -> {
                        adListenerRef.set(invocation.getArgument(0));
                        return mockBuilder;
                    });
                    when(mockBuilder.build()).thenReturn(mock(AdLoader.class));
                })) {

            NativeAdLoader.load(context, container, R.layout.ad_home_banner_large, adRequest, externalListener);

            AdListener adListener = adListenerRef.get();
            assertNotNull(adListener);
            adListener.onAdFailedToLoad(loadAdError);

            assertTrue(mainThreadExecutor.hasPendingTasks());
            verify(container, never()).removeAllViews();
            verify(container, never()).setVisibility(View.GONE);
            verify(externalListener, never()).onAdFailedToLoad(loadAdError);

            mainThreadExecutor.drain();

            verify(container).removeAllViews();
            verify(container).setVisibility(View.GONE);
            verify(externalListener, times(1)).onAdFailedToLoad(loadAdError);
            assertFalse(mainThreadExecutor.hasPendingTasks());
        }
    }

    private static final class TestMainThreadExecutor implements NativeAdLoader.MainThreadExecutor {
        private final Queue<Runnable> tasks = new ArrayDeque<>();

        @Override
        public void post(Runnable runnable) {
            tasks.add(runnable);
        }

        void drain() {
            while (!tasks.isEmpty()) {
                tasks.poll().run();
            }
        }

        boolean hasPendingTasks() {
            return !tasks.isEmpty();
        }
    }
}
