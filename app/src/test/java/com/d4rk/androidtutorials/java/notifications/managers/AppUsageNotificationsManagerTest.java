package com.d4rk.androidtutorials.java.notifications.managers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.TimeUnit;

/**
 * Tests for {@link AppUsageNotificationsManager}.
 */
public class AppUsageNotificationsManagerTest {

    @Test
    public void scheduleAppUsageCheck_setsRepeatingAlarmWithThreeDayInterval() {
        AlarmManager alarmManager = mock(AlarmManager.class);
        Context context = mock(Context.class);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);
        when(context.getApplicationContext()).thenReturn(context);

        long now = System.currentTimeMillis();
        AppUsageNotificationsManager manager = new AppUsageNotificationsManager(context);

        manager.scheduleAppUsageCheck();

        ArgumentCaptor<Long> triggerCaptor = ArgumentCaptor.forClass(Long.class);
        verify(alarmManager).setRepeating(
                eq(AlarmManager.RTC_WAKEUP),
                triggerCaptor.capture(),
                eq(TimeUnit.DAYS.toMillis(3)),
                any(PendingIntent.class)
        );

        long expectedTrigger = now + TimeUnit.DAYS.toMillis(3);
        assertTrue(Math.abs(triggerCaptor.getValue() - expectedTrigger) < 1000);
    }
}

