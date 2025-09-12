package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.d4rk.androidtutorials.java.notifications.receivers.QuizReminderReceiver;

import java.util.concurrent.TimeUnit;

/**
 * Utility for scheduling daily quiz reminder notifications.
 */
public class QuizReminderManager {

    private final AlarmManager alarmManager;
    private final PendingIntent reminderIntent;

    public QuizReminderManager(Context context) {
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        reminderIntent = PendingIntent.getBroadcast(
                context,
                1,
                new Intent(context, QuizReminderReceiver.class),
                PendingIntent.FLAG_IMMUTABLE
        );
    }

    /**
     * Schedule a repeating daily reminder.
     */
    public void scheduleDailyReminder() {
        long trigger = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1);
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                trigger,
                TimeUnit.DAYS.toMillis(1),
                reminderIntent
        );
    }
}
