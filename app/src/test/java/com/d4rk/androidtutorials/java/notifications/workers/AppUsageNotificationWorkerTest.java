package com.d4rk.androidtutorials.java.notifications.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.core.app.NotificationCompat;

import com.d4rk.androidtutorials.java.R;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppUsageNotificationWorkerTest {

    @Test
    public void doWork_whenLastUsedExceedsThreshold_createsChannelAndNotifies() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);

        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);

        long threshold = TimeUnit.DAYS.toMillis(3);
        long currentTime = System.currentTimeMillis();
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(currentTime - threshold - 1L);

        WorkerParameters parameters = mock(WorkerParameters.class);

        try (MockedStatic<PreferenceManager> mockedPreferences = mockStatic(PreferenceManager.class)) {
            mockedPreferences.when(() -> PreferenceManager.getDefaultSharedPreferences(context))
                    .thenReturn(sharedPreferences);

            when(context.getString(R.string.app_usage_notifications)).thenReturn("App usage");
            when(context.getString(R.string.notification_last_time_used_title)).thenReturn("We miss you");
            when(context.getString(R.string.summary_notification_last_time_used)).thenReturn("Come back soon");

            Notification notification = mock(Notification.class);

            try (MockedConstruction<NotificationCompat.Builder> mockedBuilder =
                         Mockito.mockConstruction(NotificationCompat.Builder.class, (builderMock, contextData) -> {
                             when(builderMock.setSmallIcon(anyInt())).thenReturn(builderMock);
                             when(builderMock.setContentTitle(any(CharSequence.class))).thenReturn(builderMock);
                             when(builderMock.setContentText(any(CharSequence.class))).thenReturn(builderMock);
                             when(builderMock.setAutoCancel(anyBoolean())).thenReturn(builderMock);
                             when(builderMock.build()).thenReturn(notification);
                         })) {

                AppUsageNotificationWorker worker = new AppUsageNotificationWorker(context, parameters);
                ListenableWorker.Result result = worker.doWork();

                assertEquals(ListenableWorker.Result.success(), result);

                ArgumentCaptor<NotificationChannel> channelCaptor = ArgumentCaptor.forClass(NotificationChannel.class);
                verify(notificationManager).createNotificationChannel(channelCaptor.capture());
                NotificationChannel channel = channelCaptor.getValue();
                assertEquals("app_usage_channel", channel.getId());
                assertEquals("App usage", channel.getName().toString());

                verify(notificationManager).notify(eq(0), eq(notification));

                ArgumentCaptor<Long> timestampCaptor = ArgumentCaptor.forClass(Long.class);
                verify(editor).putLong(eq("lastUsed"), timestampCaptor.capture());
                assertTrue(timestampCaptor.getValue() >= currentTime);
                verify(editor).apply();

                assertEquals(1, mockedBuilder.constructed().size());
            }
        }
    }

    @Test
    public void doWork_whenLastUsedWithinThreshold_skipsNotification() {
        Context context = mock(Context.class);
        when(context.getApplicationContext()).thenReturn(context);
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);

        SharedPreferences sharedPreferences = mock(SharedPreferences.class);
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);

        long threshold = TimeUnit.DAYS.toMillis(3);
        long currentTime = System.currentTimeMillis();
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(currentTime - threshold + 1L);

        WorkerParameters parameters = mock(WorkerParameters.class);

        try (MockedStatic<PreferenceManager> mockedPreferences = mockStatic(PreferenceManager.class)) {
            mockedPreferences.when(() -> PreferenceManager.getDefaultSharedPreferences(context))
                    .thenReturn(sharedPreferences);

            when(context.getString(R.string.app_usage_notifications)).thenReturn("App usage");
            when(context.getString(R.string.notification_last_time_used_title)).thenReturn("We miss you");
            when(context.getString(R.string.summary_notification_last_time_used)).thenReturn("Come back soon");

            try (MockedConstruction<NotificationCompat.Builder> mockedBuilder =
                         Mockito.mockConstruction(NotificationCompat.Builder.class)) {
                AppUsageNotificationWorker worker = new AppUsageNotificationWorker(context, parameters);
                ListenableWorker.Result result = worker.doWork();

                assertEquals(ListenableWorker.Result.success(), result);

                verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
                verify(notificationManager, never()).notify(anyInt(), any(Notification.class));

                verify(editor).putLong(eq("lastUsed"), anyLong());
                verify(editor).apply();

                assertTrue(mockedBuilder.constructed().isEmpty());
            }
        }
    }
}
