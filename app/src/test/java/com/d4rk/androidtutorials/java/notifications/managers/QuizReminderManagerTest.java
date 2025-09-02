package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class QuizReminderManagerTest {

    @Test
    public void scheduleDailyReminderSetsRepeatingAlarm() throws Exception {
        Context context = mock(Context.class);
        AlarmManager alarmManager = mock(AlarmManager.class);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);
        when(context.getApplicationContext()).thenReturn(context);

        QuizReminderManager manager = new QuizReminderManager(context);

        PendingIntent pendingIntent = mock(PendingIntent.class);
        Field intentField = QuizReminderManager.class.getDeclaredField("reminderIntent");
        intentField.setAccessible(true);
        intentField.set(manager, pendingIntent);

        Field alarmField = QuizReminderManager.class.getDeclaredField("alarmManager");
        alarmField.setAccessible(true);
        alarmField.set(manager, alarmManager);

        long now = System.currentTimeMillis();
        manager.scheduleDailyReminder();

        ArgumentCaptor<Long> triggerCaptor = ArgumentCaptor.forClass(Long.class);
        verify(alarmManager).setRepeating(eq(AlarmManager.RTC_WAKEUP),
                triggerCaptor.capture(),
                eq(TimeUnit.DAYS.toMillis(1)),
                eq(pendingIntent));

        long expected = now + TimeUnit.DAYS.toMillis(1);
        assertTrue(Math.abs(triggerCaptor.getValue() - expected) < TimeUnit.MINUTES.toMillis(1));
    }
}
