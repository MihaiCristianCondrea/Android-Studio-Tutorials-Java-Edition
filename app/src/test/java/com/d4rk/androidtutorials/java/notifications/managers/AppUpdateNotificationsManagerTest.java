package com.d4rk.androidtutorials.java.notifications.managers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;

/**
 * Unit tests for {@link AppUpdateNotificationsManager}.
 */
public class AppUpdateNotificationsManagerTest {

    @Test
    public void checkAndSendUpdateNotification_updateAvailableAndAllowed_sendsNotification() {
        Context context = mock(Context.class);
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);
        when(context.getString(anyInt())).thenReturn("test");
        when(context.getPackageName()).thenReturn("com.test");

        AppUpdateManager appUpdateManager = mock(AppUpdateManager.class);
        AppUpdateInfo appUpdateInfo = mock(AppUpdateInfo.class);
        when(appUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_AVAILABLE);
        when(appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)).thenReturn(true);

        Task<AppUpdateInfo> task = mock(Task.class);
        doAnswer(invocation -> {
            OnSuccessListener<AppUpdateInfo> listener = invocation.getArgument(0);
            listener.onSuccess(appUpdateInfo);
            return task;
        }).when(task).addOnSuccessListener(any());
        when(appUpdateManager.getAppUpdateInfo()).thenReturn(task);

        try (MockedStatic<AppUpdateManagerFactory> factory = mockStatic(AppUpdateManagerFactory.class);
             MockedStatic<PendingIntent> pendingIntent = mockStatic(PendingIntent.class);
             MockedConstruction<NotificationChannel> channel = mockConstruction(NotificationChannel.class)) {
            factory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(appUpdateManager);
            pendingIntent.when(() -> PendingIntent.getActivity(eq(context), anyInt(), any(Intent.class), anyInt()))
                    .thenReturn(mock(PendingIntent.class));

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context);
            manager.checkAndSendUpdateNotification();
        }

        verify(notificationManager).createNotificationChannel(any(NotificationChannel.class));
        verify(notificationManager).notify(eq(0), any());
    }

    @Test
    public void checkAndSendUpdateNotification_updateNotAvailable_doesNotSendNotification() {
        Context context = mock(Context.class);
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);
        when(context.getString(anyInt())).thenReturn("test");
        when(context.getPackageName()).thenReturn("com.test");

        AppUpdateManager appUpdateManager = mock(AppUpdateManager.class);
        AppUpdateInfo appUpdateInfo = mock(AppUpdateInfo.class);
        when(appUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_NOT_AVAILABLE);

        Task<AppUpdateInfo> task = mock(Task.class);
        doAnswer(invocation -> {
            OnSuccessListener<AppUpdateInfo> listener = invocation.getArgument(0);
            listener.onSuccess(appUpdateInfo);
            return task;
        }).when(task).addOnSuccessListener(any());
        when(appUpdateManager.getAppUpdateInfo()).thenReturn(task);

        try (MockedStatic<AppUpdateManagerFactory> factory = mockStatic(AppUpdateManagerFactory.class);
             MockedStatic<PendingIntent> pendingIntent = mockStatic(PendingIntent.class);
             MockedConstruction<NotificationChannel> channel = mockConstruction(NotificationChannel.class)) {
            factory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(appUpdateManager);
            pendingIntent.when(() -> PendingIntent.getActivity(eq(context), anyInt(), any(Intent.class), anyInt()))
                    .thenReturn(mock(PendingIntent.class));

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context);
            manager.checkAndSendUpdateNotification();
        }

        verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
        verify(notificationManager, never()).notify(anyInt(), any());
    }

    @Test
    public void checkAndSendUpdateNotification_updateNotAllowed_doesNotSendNotification() {
        Context context = mock(Context.class);
        NotificationManager notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);
        when(context.getString(anyInt())).thenReturn("test");
        when(context.getPackageName()).thenReturn("com.test");

        AppUpdateManager appUpdateManager = mock(AppUpdateManager.class);
        AppUpdateInfo appUpdateInfo = mock(AppUpdateInfo.class);
        when(appUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_AVAILABLE);
        when(appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)).thenReturn(false);

        Task<AppUpdateInfo> task = mock(Task.class);
        doAnswer(invocation -> {
            OnSuccessListener<AppUpdateInfo> listener = invocation.getArgument(0);
            listener.onSuccess(appUpdateInfo);
            return task;
        }).when(task).addOnSuccessListener(any());
        when(appUpdateManager.getAppUpdateInfo()).thenReturn(task);

        try (MockedStatic<AppUpdateManagerFactory> factory = mockStatic(AppUpdateManagerFactory.class);
             MockedStatic<PendingIntent> pendingIntent = mockStatic(PendingIntent.class);
             MockedConstruction<NotificationChannel> channel = mockConstruction(NotificationChannel.class)) {
            factory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(appUpdateManager);
            pendingIntent.when(() -> PendingIntent.getActivity(eq(context), anyInt(), any(Intent.class), anyInt()))
                    .thenReturn(mock(PendingIntent.class));

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context);
            manager.checkAndSendUpdateNotification();
        }

        verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
        verify(notificationManager, never()).notify(anyInt(), any());
    }
}

