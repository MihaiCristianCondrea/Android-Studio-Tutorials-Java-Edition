package com.d4rk.androidtutorials.java.data.source;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

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
        long daysSinceEpoch = 5L;
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(context, fixedClockForDays(daysSinceEpoch));

        int expectedIndex = (int) (daysSinceEpoch % tips.length);
        assertEquals(tips[expectedIndex], dataSource.getDailyTip());
    }

    @Test
    public void dailyTipCyclesAcrossDays() {
        String[] tips = {"tip1", "tip2", "tip3"};
        Context context = mockContextWithTips(tips);

        DefaultHomeLocalDataSource dayZero =
                new DefaultHomeLocalDataSource(context, fixedClockForDays(0));
        DefaultHomeLocalDataSource dayOne =
                new DefaultHomeLocalDataSource(context, fixedClockForDays(1));
        DefaultHomeLocalDataSource dayTwo =
                new DefaultHomeLocalDataSource(context, fixedClockForDays(2));
        DefaultHomeLocalDataSource dayThree =
                new DefaultHomeLocalDataSource(context, fixedClockForDays(3));

        assertEquals("tip1", dayZero.getDailyTip());
        assertEquals("tip2", dayOne.getDailyTip());
        assertEquals("tip3", dayTwo.getDailyTip());
        assertEquals("tip1", dayThree.getDailyTip());
    }

    @Test
    public void dailyTipThrowsWhenNoTipsAvailable() {
        Context context = mockContextWithTips(new String[0]);
        DefaultHomeLocalDataSource dataSource =
                new DefaultHomeLocalDataSource(context, fixedClockForDays(0));

        assertThrows(IllegalStateException.class, dataSource::getDailyTip);
    }

    private static Context mockContextWithTips(String[] tips) {
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getResources()).thenReturn(resources);
        when(resources.getStringArray(R.array.daily_tips)).thenReturn(tips);
        return context;
    }

    private static Clock fixedClockForDays(long days) {
        long millisPerDay = 24L * 60L * 60L * 1000L;
        return Clock.fixed(Instant.ofEpochMilli(days * millisPerDay), ZoneOffset.UTC);
    }
}
