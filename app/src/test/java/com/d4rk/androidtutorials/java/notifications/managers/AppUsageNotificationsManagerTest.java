package com.d4rk.androidtutorials.java.notifications.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.app.AlarmManager;
import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlarmManager;
import org.robolectric.shadows.ShadowPendingIntent;
import org.robolectric.shadows.ShadowSystemClock;
import org.robolectric.Shadows;

import java.util.List;
import java.util.concurrent.TimeUnit;

import android.app.PendingIntent;

import com.d4rk.androidtutorials.java.notifications.receivers.AppUsageNotificationReceiver;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 26)
public class AppUsageNotificationsManagerTest {

    @Test
    public void scheduleAppUsageCheck_setsRepeatingAlarm() {
        Context context = ApplicationProvider.getApplicationContext();
        long now = 1_000L;
        ShadowSystemClock.setCurrentTimeMillis(now);

        AppUsageNotificationsManager manager = new AppUsageNotificationsManager(context);
        manager.scheduleAppUsageCheck();

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        ShadowAlarmManager shadowAlarmManager = Shadows.shadowOf(alarmManager);
        List<ShadowAlarmManager.ScheduledAlarm> alarms = shadowAlarmManager.getScheduledAlarms();
        assertEquals(1, alarms.size());
        ShadowAlarmManager.ScheduledAlarm alarm = alarms.get(0);

        assertEquals(AlarmManager.RTC_WAKEUP, alarm.type);
        assertEquals(now + TimeUnit.DAYS.toMillis(3), alarm.triggerAtTime);
        assertEquals(TimeUnit.DAYS.toMillis(3), alarm.interval);

        PendingIntent pendingIntent = alarm.operation;
        ShadowPendingIntent shadowPendingIntent = Shadows.shadowOf(pendingIntent);
        assertEquals(AppUsageNotificationReceiver.class.getName(), shadowPendingIntent.getSavedIntent().getComponent().getClassName());
    }
}
