package com.d4rk.androidtutorials.java.data.source;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;
import org.mockito.MockedStatic;

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
        Context context = mockContextWithTips(new String[]{"tip1", "tip2", "tip3"});
        DefaultHomeLocalDataSource dataSource = new DefaultHomeLocalDataSource(context);

        long days = 5L; // 5 days since epoch -> index = 2
        long millis = days * 24L * 60L * 60L * 1000L;

        try (MockedStatic<System> mocked = mockStatic(System.class)) {
            mocked.when(System::currentTimeMillis).thenReturn(millis);
            assertEquals("tip3", dataSource.getDailyTip());
        }
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
