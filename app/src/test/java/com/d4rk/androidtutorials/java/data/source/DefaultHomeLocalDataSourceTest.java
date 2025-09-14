package com.d4rk.androidtutorials.java.data.source;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;

public class DefaultHomeLocalDataSourceTest {

    @Test
    public void playStoreUrlsFormattedCorrectly() {
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(mockContextWithTips(new String[]{"tip"}));
        assertEquals("https://play.google.com/store/apps/details?id=com.d4rk.androidtutorials", dataSource.getPlayStoreUrl());
        assertEquals("https://play.google.com/store/apps/details?id=pkg", dataSource.getAppPlayStoreUrl("pkg"));
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
