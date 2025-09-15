package com.d4rk.androidtutorials.java.startup;

import android.content.Context;
import android.webkit.CookieManager;

import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.d4rk.androidtutorials.java.ads.AdUtils;

import org.junit.Test;
import org.mockito.MockedStatic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class StartupWorkerTest {

    @Test
    public void doWork_initializesAdsAndCookiesAndReturnsSuccess() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        WorkerParameters parameters = mock(WorkerParameters.class);
        StartupWorker worker = new StartupWorker(context, parameters);

        try (MockedStatic<AdUtils> mockedAdUtils = mockStatic(AdUtils.class);
             MockedStatic<CookieManager> mockedCookieManager = mockStatic(CookieManager.class)) {
            mockedCookieManager.when(CookieManager::getInstance).thenReturn(mock(CookieManager.class));

            ListenableWorker.Result result = worker.doWork();

            mockedAdUtils.verify(() -> AdUtils.initialize(context));
            mockedCookieManager.verify(CookieManager::getInstance);
            assertEquals(ListenableWorker.Result.success(), result);
        }
    }
}
