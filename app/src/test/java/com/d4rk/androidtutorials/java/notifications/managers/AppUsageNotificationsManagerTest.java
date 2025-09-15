package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.d4rk.androidtutorials.java.notifications.receivers.AppUsageNotificationReceiver;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppUsageNotificationsManagerTest {

    @Test
    public void scheduleAppUsageCheck_setsRepeatingAlarmForReceiver() {
        Context context = mock(Context.class);
        AlarmManager alarmManager = mock(AlarmManager.class);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);

        PendingIntent pendingIntent = mock(PendingIntent.class);
        AtomicReference<Intent> capturedIntent = new AtomicReference<>();

        try (MockedStatic<PendingIntent> mockedPendingIntent = mockStatic(PendingIntent.class)) {
            mockedPendingIntent.when(() -> PendingIntent.getBroadcast(
                    eq(context),
                    eq(0),
                    any(Intent.class),
                    eq(PendingIntent.FLAG_IMMUTABLE)
            )).thenAnswer(invocation -> {
                Intent intent = invocation.getArgument(2);
                capturedIntent.set(intent);
                return pendingIntent;
            });

            long beforeCall = System.currentTimeMillis();
            AppUsageNotificationsManager manager = new AppUsageNotificationsManager(context);
            manager.scheduleAppUsageCheck();
            long afterCall = System.currentTimeMillis();

            ArgumentCaptor<Long> triggerCaptor = ArgumentCaptor.forClass(Long.class);
            verify(alarmManager).setRepeating(
                    eq(AlarmManager.RTC_WAKEUP),
                    triggerCaptor.capture(),
                    eq(TimeUnit.DAYS.toMillis(3)),
                    eq(pendingIntent)
            );

            long interval = TimeUnit.DAYS.toMillis(3);
            long triggerTime = triggerCaptor.getValue();
            long scheduledOrigin = triggerTime - interval;
            assertTrue(scheduledOrigin >= beforeCall);
            assertTrue(scheduledOrigin <= afterCall);

            Intent intent = capturedIntent.get();
            assertNotNull("PendingIntent should target the usage receiver", intent);
            assertNotNull("PendingIntent should include a component", intent.getComponent());
            assertEquals(AppUsageNotificationReceiver.class.getName(), intent.getComponent().getClassName());
        }
    }
}
