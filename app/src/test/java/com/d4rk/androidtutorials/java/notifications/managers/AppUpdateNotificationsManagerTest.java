package com.d4rk.androidtutorials.java.notifications.managers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.Shadows;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O)
public class AppUpdateNotificationsManagerTest {

    @Test
    public void checkAndSendUpdateNotification_createsChannelAndNotification() {
        Context context = ApplicationProvider.getApplicationContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ShadowNotificationManager shadowNotificationManager = Shadows.shadowOf(notificationManager);

        AppUpdateManager mockAppUpdateManager = mock(AppUpdateManager.class);
        AppUpdateInfo mockAppUpdateInfo = mock(AppUpdateInfo.class);
        Task<AppUpdateInfo> task = Tasks.forResult(mockAppUpdateInfo);
        when(mockAppUpdateManager.getAppUpdateInfo()).thenReturn(task);
        when(mockAppUpdateInfo.updateAvailability()).thenReturn(UpdateAvailability.UPDATE_AVAILABLE);
        when(mockAppUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)).thenReturn(true);

        try (MockedStatic<AppUpdateManagerFactory> mockedStatic = Mockito.mockStatic(AppUpdateManagerFactory.class)) {
            mockedStatic.when(() -> AppUpdateManagerFactory.create(context)).thenReturn(mockAppUpdateManager);

            AppUpdateNotificationsManager manager = new AppUpdateNotificationsManager(context);
            manager.checkAndSendUpdateNotification();

            assertNotNull(shadowNotificationManager.getNotificationChannel("update_channel"));
            assertEquals(1, shadowNotificationManager.getAllNotifications().size());
        }
    }
}
