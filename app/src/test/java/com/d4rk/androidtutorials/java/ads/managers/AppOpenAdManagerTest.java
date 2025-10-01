package com.d4rk.androidtutorials.java.ads.managers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.ads.managers.AppOpenAd.OnShowAdCompleteListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * Unit tests for AppOpenAd.AppOpenAdManager.
 */
public class AppOpenAdManagerTest {

    private Class<?> managerClass;
    private Object manager;
    private Application application;

    @Before
    public void setUp() throws Exception {
        application = mock(Application.class);
        when(application.getApplicationContext()).thenReturn(application);
        when(application.getString(anyInt())).thenReturn("test-app-open-ad-unit-id");

        managerClass = findManagerClass();
        Constructor<?> constructor = managerClass.getDeclaredConstructor(Application.class);
        constructor.setAccessible(true);
        manager = constructor.newInstance(application);
    }

    @Test
    public void isAdAvailable_returnsFalseWhenAdIsNull() throws Exception {
        setField("appOpenAd", null);
        setLongField("loadTime", System.currentTimeMillis());

        assertFalse(invokeIsAdAvailable());
    }

    @Test
    public void isAdAvailable_returnsFalseWhenLoadTimeTooOld() throws Exception {
        com.google.android.gms.ads.appopen.AppOpenAd ad = mock(com.google.android.gms.ads.appopen.AppOpenAd.class);
        setField("appOpenAd", ad);
        long staleTime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(5);
        setLongField("loadTime", staleTime);

        assertFalse(invokeIsAdAvailable());
    }

    @Test
    public void isAdAvailable_returnsTrueWhenAdFresh() throws Exception {
        com.google.android.gms.ads.appopen.AppOpenAd ad = mock(com.google.android.gms.ads.appopen.AppOpenAd.class);
        setField("appOpenAd", ad);
        long recentTime = System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1);
        setLongField("loadTime", recentTime);

        assertTrue(invokeIsAdAvailable());
    }

    @Test
    public void showAdIfAvailable_withoutAd_loadsNewAd() throws Exception {
        Activity activity = mock(Activity.class);
        OnShowAdCompleteListener listener = mock(OnShowAdCompleteListener.class);

        try (MockedStatic<AdUtils> adUtils = mockStatic(AdUtils.class);
             MockedStatic<com.google.android.gms.ads.appopen.AppOpenAd> appOpenAdStatic =
                     mockStatic(com.google.android.gms.ads.appopen.AppOpenAd.class)) {
            com.google.android.gms.ads.appopen.AppOpenAd loadedAd = mock(com.google.android.gms.ads.appopen.AppOpenAd.class);

            appOpenAdStatic
                    .when(() -> com.google.android.gms.ads.appopen.AppOpenAd.load(
                            any(Context.class),
                            anyString(),
                            any(AdRequest.class),
                            any(AppOpenAdLoadCallback.class)))
                    .thenAnswer(invocation -> {
                        AppOpenAdLoadCallback callback = invocation.getArgument(3);
                        callback.onAdLoaded(loadedAd);
                        return null;
                    });

            invokeShowAdIfAvailable(activity, listener);

            adUtils.verify(() -> AdUtils.initialize(application));
            appOpenAdStatic.verify(() -> com.google.android.gms.ads.appopen.AppOpenAd.load(
                    any(Context.class),
                    anyString(),
                    any(AdRequest.class),
                    any(AppOpenAdLoadCallback.class)));
        }

        verify(listener, times(1)).onShowAdComplete();
    }

    @Test
    public void showAdIfAvailable_withAd_doesNotShowTwiceWhileShowing() throws Exception {
        Activity activity = mock(Activity.class);
        OnShowAdCompleteListener listener = mock(OnShowAdCompleteListener.class);
        com.google.android.gms.ads.appopen.AppOpenAd ad = mock(com.google.android.gms.ads.appopen.AppOpenAd.class);

        setField("appOpenAd", ad);
        setLongField("loadTime", System.currentTimeMillis());

        invokeShowAdIfAvailable(activity, listener);
        invokeShowAdIfAvailable(activity, listener);

        ArgumentCaptor<FullScreenContentCallback> callbackCaptor =
                ArgumentCaptor.forClass(FullScreenContentCallback.class);
        verify(ad, times(1)).setFullScreenContentCallback(callbackCaptor.capture());
        verify(ad, times(1)).show(activity);
    }

    private Class<?> findManagerClass() {
        for (Class<?> clazz : AppOpenAd.class.getDeclaredClasses()) {
            if ("AppOpenAdManager".equals(clazz.getSimpleName())) {
                return clazz;
            }
        }
        throw new IllegalStateException("AppOpenAdManager class not found");
    }

    private boolean invokeIsAdAvailable() throws Exception {
        Method method = managerClass.getDeclaredMethod("isAdAvailable");
        method.setAccessible(true);
        Object result = method.invoke(manager);
        if (result instanceof Boolean) {
            return (boolean) result;
        }
        throw new IllegalStateException("isAdAvailable method did not return a boolean value.");
    }

    private void invokeShowAdIfAvailable(Activity activity, OnShowAdCompleteListener listener)
            throws Exception {
        Method method = managerClass.getDeclaredMethod(
                "showAdIfAvailable",
                Activity.class,
                OnShowAdCompleteListener.class);
        method.setAccessible(true);
        method.invoke(manager, activity, listener);
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = managerClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(manager, value);
    }

    private void setLongField(String fieldName, long value) throws Exception {
        Field field = managerClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.setLong(manager, value);
    }
}
