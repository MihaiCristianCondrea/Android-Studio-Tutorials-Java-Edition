package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE, sdk = 33)
public class AppUsageNotificationsManagerTest {

    @Test
    public void scheduleAppUsageCheckSetsRepeatingAlarm() throws Exception {
        Context context = mock(Context.class);
        AlarmManager alarmManager = mock(AlarmManager.class);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);
        when(context.getApplicationContext()).thenReturn(context);

        AppUsageNotificationsManager manager = new AppUsageNotificationsManager(context);

        PendingIntent pendingIntent = mock(PendingIntent.class);
        Field intentField = AppUsageNotificationsManager.class.getDeclaredField("notificationIntent");
        intentField.setAccessible(true);
        intentField.set(manager, pendingIntent);

        Field alarmField = AppUsageNotificationsManager.class.getDeclaredField("alarmManager");
        alarmField.setAccessible(true);
        alarmField.set(manager, alarmManager);

        long now = System.currentTimeMillis();
        manager.scheduleAppUsageCheck();

        ArgumentCaptor<Long> triggerCaptor = ArgumentCaptor.forClass(Long.class);
        verify(alarmManager).setRepeating(eq(AlarmManager.RTC_WAKEUP),
                triggerCaptor.capture(),
                eq(TimeUnit.DAYS.toMillis(3)),
                eq(pendingIntent));

        long expected = now + TimeUnit.DAYS.toMillis(3);
        assertTrue(Math.abs(triggerCaptor.getValue() - expected) < TimeUnit.MINUTES.toMillis(1));
    }
}
