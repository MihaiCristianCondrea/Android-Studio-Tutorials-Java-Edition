package com.d4rk.androidtutorials.java.notifications.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.tasks.Tasks;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AppUpdateNotificationsManagerTest {

    private static final String PACKAGE_NAME = "com.d4rk.androidtutorials.java";

    private Context context;
    private NotificationManager notificationManager;
    private AppUpdateManager appUpdateManager;
    private AppUpdateInfo appUpdateInfo;

    @Before
    public void setUp() {
        context = mock(Context.class);
        notificationManager = mock(NotificationManager.class);
        when(context.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(notificationManager);
        when(context.getString(R.string.update_notifications)).thenReturn("Update notifications");
        when(context.getString(R.string.notification_update_title)).thenReturn("Update available");
        when(context.getString(R.string.summary_notification_update)).thenReturn("An update is ready");
        when(context.getPackageName()).thenReturn(PACKAGE_NAME);

        appUpdateManager = mock(AppUpdateManager.class);
        appUpdateInfo = mock(AppUpdateInfo.class);
    }

    @Test
    public void checkAndSendUpdateNotification_whenFlexibleUpdateAvailable_sendsPlayStoreNotification() {
        when(appUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_AVAILABLE);
        when(appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)).thenReturn(true);
        when(appUpdateManager.getAppUpdateInfo()).thenReturn(Tasks.forResult(appUpdateInfo));

        PendingIntent pendingIntent = mock(PendingIntent.class);
        Notification notification = new Notification();
        AtomicReference<Intent> capturedIntent = new AtomicReference<>();

        try (MockedStatic<AppUpdateManagerFactory> updateManagerFactory = Mockito.mockStatic(AppUpdateManagerFactory.class);
             MockedStatic<PendingIntent> pendingIntentMock = Mockito.mockStatic(PendingIntent.class);
             MockedConstruction<NotificationCompat.Builder> builderConstruction = mockNotificationBuilder(notification)) {

            updateManagerFactory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(appUpdateManager);

            pendingIntentMock
                    .when(() -> PendingIntent.getActivity(eq(context), eq(0), any(Intent.class), eq(PendingIntent.FLAG_IMMUTABLE)))
                    .thenAnswer(invocation -> {
                        Intent intent = invocation.getArgument(2);
                        capturedIntent.set(intent);
                        return pendingIntent;
                    });

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context, () -> Build.VERSION_CODES.O);
            manager.checkAndSendUpdateNotification();

            List<NotificationCompat.Builder> builders = builderConstruction.constructed();
            assertEquals(1, builders.size());
            NotificationCompat.Builder builder = builders.get(0);
            verify(builder).setContentIntent(pendingIntent);

            verify(notificationManager).createNotificationChannel(any(NotificationChannel.class));
            verify(notificationManager).notify(eq(0), same(notification));

            Intent launchIntent = capturedIntent.get();
            assertNotNull(launchIntent);
            assertEquals(Intent.ACTION_VIEW, launchIntent.getAction());
            assertEquals(Uri.parse("market://details?id=" + PACKAGE_NAME), launchIntent.getData());
        }
    }

    @Test
    public void checkAndSendUpdateNotification_whenUpdateUnavailable_doesNotSendNotification() {
        when(appUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_NOT_AVAILABLE);
        when(appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)).thenReturn(false);
        when(appUpdateManager.getAppUpdateInfo()).thenReturn(Tasks.forResult(appUpdateInfo));

        Notification notification = new Notification();

        try (MockedStatic<AppUpdateManagerFactory> updateManagerFactory = Mockito.mockStatic(AppUpdateManagerFactory.class);
             MockedStatic<PendingIntent> pendingIntentMock = Mockito.mockStatic(PendingIntent.class);
             MockedConstruction<NotificationCompat.Builder> builderConstruction = mockNotificationBuilder(notification)) {

            updateManagerFactory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(appUpdateManager);

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context, () -> Build.VERSION_CODES.O);
            manager.checkAndSendUpdateNotification();

            verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
            verify(notificationManager, never()).notify(anyInt(), any(Notification.class));
            assertTrue(builderConstruction.constructed().isEmpty());
            pendingIntentMock.verifyNoInteractions();
        }
    }

    @Test
    public void checkAndSendUpdateNotification_createsChannelOnlyOnOreoOrHigher() {
        when(appUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_AVAILABLE);
        when(appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)).thenReturn(true);
        when(appUpdateManager.getAppUpdateInfo()).thenReturn(Tasks.forResult(appUpdateInfo));

        PendingIntent pendingIntent = mock(PendingIntent.class);
        Notification notification = new Notification();

        try (MockedStatic<AppUpdateManagerFactory> updateManagerFactory = Mockito.mockStatic(AppUpdateManagerFactory.class);
             MockedStatic<PendingIntent> pendingIntentMock = Mockito.mockStatic(PendingIntent.class);
             MockedConstruction<NotificationCompat.Builder> builderConstruction = mockNotificationBuilder(notification)) {

            updateManagerFactory.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(appUpdateManager);
            pendingIntentMock.when(() -> PendingIntent.getActivity(eq(context), eq(0), any(Intent.class), eq(PendingIntent.FLAG_IMMUTABLE)))
                    .thenReturn(pendingIntent);

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context, () -> Build.VERSION_CODES.N_MR1);
            manager.checkAndSendUpdateNotification();

            verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
            verify(notificationManager).notify(eq(0), same(notification));

            List<NotificationCompat.Builder> builders = builderConstruction.constructed();
            assertEquals(1, builders.size());
            verify(builders.get(0)).setContentIntent(pendingIntent);
        }
    }

    private MockedConstruction<NotificationCompat.Builder> mockNotificationBuilder(Notification notification) {
        return Mockito.mockConstruction(NotificationCompat.Builder.class, (mockBuilder, context) -> {
            when(mockBuilder.setSmallIcon(anyInt())).thenReturn(mockBuilder);
            when(mockBuilder.setContentTitle(anyString())).thenReturn(mockBuilder);
            when(mockBuilder.setContentText(anyString())).thenReturn(mockBuilder);
            when(mockBuilder.setAutoCancel(anyBoolean())).thenReturn(mockBuilder);
            when(mockBuilder.setContentIntent(any(PendingIntent.class))).thenReturn(mockBuilder);
            when(mockBuilder.build()).thenReturn(notification);
        });
    }
}
