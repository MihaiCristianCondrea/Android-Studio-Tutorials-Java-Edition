package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AppUsageNotificationsManagerTest {

    @Test
    public void scheduleAppUsageCheck_setsRepeatingAlarm() {
        AlarmManager alarmManager = mock(AlarmManager.class);
        PendingIntent pendingIntent = mock(PendingIntent.class);
        Context context = mock(Context.class);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);

        try (MockedStatic<PendingIntent> pendingStatic = mockStatic(PendingIntent.class)) {
            pendingStatic.when(() -> PendingIntent.getBroadcast(any(Context.class), anyInt(), any(Intent.class), anyInt()))
                    .thenReturn(pendingIntent);

            AppUsageNotificationsManager manager = new AppUsageNotificationsManager(context);
            long now = System.currentTimeMillis();
            manager.scheduleAppUsageCheck();

            ArgumentCaptor<Long> triggerCaptor = ArgumentCaptor.forClass(Long.class);
            verify(alarmManager).setRepeating(eq(AlarmManager.RTC_WAKEUP), triggerCaptor.capture(),
                    eq(TimeUnit.DAYS.toMillis(3)), eq(pendingIntent));

            long expected = now + TimeUnit.DAYS.toMillis(3);
            assertTrue(Math.abs(triggerCaptor.getValue() - expected) < TimeUnit.SECONDS.toMillis(1));
        }
    }
}
