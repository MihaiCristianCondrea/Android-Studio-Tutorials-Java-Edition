package com.d4rk.androidtutorials.java.notifications.workers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.work.WorkerParameters;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

/**
 * Tests for {@link AppUsageNotificationWorker}.
 */
public class AppUsageNotificationWorkerTest {

    @Test
    public void doWork_lastUsedExceedsThreshold_showsNotificationAndUpdatesTimestamp() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getString(anyInt())).thenReturn("");
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);

        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(0L);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);

        try (MockedStatic<PreferenceManager> prefManager = mockStatic(PreferenceManager.class);
             MockedConstruction<NotificationChannel> ignoredChannel = mockConstruction(NotificationChannel.class)) {
            prefManager.when(() -> PreferenceManager.getDefaultSharedPreferences(context))
                    .thenReturn(sharedPreferences);

            WorkerParameters workerParameters = mock(WorkerParameters.class);
            AppUsageNotificationWorker worker = new AppUsageNotificationWorker(context, workerParameters);

            worker.doWork();
        }

        verify(notificationManager, times(1)).notify(eq(0), any(Notification.class));

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(editor).putLong(eq("lastUsed"), captor.capture());
        verify(editor).apply();
        assertTrue(captor.getValue() > 0L);
    }

    @Test
    public void doWork_lastUsedWithinThreshold_noNotificationButTimestampUpdated() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        when(context.getString(anyInt())).thenReturn("");
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);

        long lastUsed = System.currentTimeMillis();
        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(lastUsed);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);

        try (MockedStatic<PreferenceManager> prefManager = mockStatic(PreferenceManager.class);
             MockedConstruction<NotificationChannel> ignoredChannel = mockConstruction(NotificationChannel.class)) {
            prefManager.when(() -> PreferenceManager.getDefaultSharedPreferences(context))
                    .thenReturn(sharedPreferences);

            WorkerParameters workerParameters = mock(WorkerParameters.class);
            AppUsageNotificationWorker worker = new AppUsageNotificationWorker(context, workerParameters);

            worker.doWork();
        }

        verify(notificationManager, never()).notify(anyInt(), any(Notification.class));

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(editor).putLong(eq("lastUsed"), captor.capture());
        verify(editor).apply();
        assertTrue(captor.getValue() >= lastUsed);
    }
}

