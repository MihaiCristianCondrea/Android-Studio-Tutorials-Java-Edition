package com.d4rk.androidtutorials.java.data.source.local;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.data.source.DefaultHomeLocalDataSource;

import org.junit.Test;

public class DefaultHomeLocalDataSourceTest {

    private static final String PLAY_STORE_BASE_URL = "https://play.google.com/store/apps/details?id=";

    @Test
    public void getPlayStoreUrl_returnsBaseUrl() {
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(mockContextWithTips(new String[]{"tip"}));

        assertEquals(PLAY_STORE_BASE_URL, dataSource.getPlayStoreUrl());
    }

    @Test
    public void getAppPlayStoreUrl_appendsPackageName() {
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(mockContextWithTips(new String[]{"tip"}));

        assertEquals(PLAY_STORE_BASE_URL + "com.example.app",
                dataSource.getAppPlayStoreUrl("com.example.app"));
    }

    @Test
    public void getAppPlayStoreUrl_allowsEmptyPackageName() {
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(mockContextWithTips(new String[]{"tip"}));

        assertEquals(PLAY_STORE_BASE_URL, dataSource.getAppPlayStoreUrl(""));
    }

    @Test
    public void getAppPlayStoreUrl_handlesNullPackageName() {
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(mockContextWithTips(new String[]{"tip"}));

        assertEquals(PLAY_STORE_BASE_URL, dataSource.getAppPlayStoreUrl(null));
    }

    @Test
    public void dailyTipUsesEpochDayIndex() {
        String[] tips = {"tip1", "tip2", "tip3"};
        Context context = mockContextWithTips(tips);
        DefaultHomeLocalDataSource dataSource = new DefaultHomeLocalDataSource(context);

        long daysSinceEpoch = System.currentTimeMillis() / (24L * 60L * 60L * 1000L);
        int expectedIndex = (int) (daysSinceEpoch % tips.length);
        assertEquals(tips[expectedIndex], dataSource.getDailyTip());
    }

    private static Context mockContextWithTips(String[] tips) {
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.daily_tips)).thenReturn(tips);
        return context;
    }
}
