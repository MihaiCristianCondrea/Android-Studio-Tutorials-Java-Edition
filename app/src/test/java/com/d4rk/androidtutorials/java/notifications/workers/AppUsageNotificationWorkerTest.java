package com.d4rk.androidtutorials.java.notifications.workers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.os.Build;
import android.test.mock.MockContext;

import androidx.work.WorkerParameters;

import com.d4rk.androidtutorials.java.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

/**
 * Tests for {@link AppUsageNotificationWorker}.
 */
public class AppUsageNotificationWorkerTest {

    private static final String PACKAGE_NAME = "com.d4rk.androidtutorials.java";

    private AutoCloseable closeable;

    @Mock
    private SharedPreferences sharedPreferences;

    @Mock
    private SharedPreferences.Editor editor;

    @Mock
    private NotificationManager notificationManager;

    @Mock
    private WorkerParameters workerParameters;

    @Mock
    private Resources resources;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

        when(sharedPreferences.edit()).thenReturn(editor);
        when(editor.putLong(anyString(), anyLong())).thenReturn(editor);

        when(resources.getString(R.string.app_usage_notifications))
                .thenReturn("App usage notifications");
        when(resources.getString(R.string.notification_last_time_used_title))
                .thenReturn("We miss you");
        when(resources.getString(R.string.summary_notification_last_time_used))
                .thenReturn("Come back and code with us");
        when(resources.getResourcePackageName(R.drawable.ic_notification_important))
                .thenReturn(PACKAGE_NAME);
        when(resources.getResourceTypeName(R.drawable.ic_notification_important))
                .thenReturn("drawable");
        when(resources.getResourceEntryName(R.drawable.ic_notification_important))
                .thenReturn("ic_notification_important");
        when(resources.getResourceName(R.drawable.ic_notification_important))
                .thenReturn(PACKAGE_NAME + ":drawable/ic_notification_important");
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void doWork_postsNotificationWhenBeyondThreshold() {
        long threshold = TimeUnit.DAYS.toMillis(3);
        long lastUsed = System.currentTimeMillis() - threshold - TimeUnit.HOURS.toMillis(1);
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(lastUsed);

        AppUsageNotificationWorker worker = createWorker();

        worker.doWork();

        verify(sharedPreferences).getLong("lastUsed", 0);
        verify(notificationManager).createNotificationChannel(any(NotificationChannel.class));
        verify(notificationManager).notify(eq(0), any(Notification.class));

        ArgumentCaptor<Long> timestampCaptor = ArgumentCaptor.forClass(Long.class);
        verify(editor).putLong(eq("lastUsed"), timestampCaptor.capture());
        verify(editor).apply();

        long updatedTimestamp = timestampCaptor.getValue();
        assertTrue(updatedTimestamp > lastUsed);
    }

    @Test
    public void doWork_skipsNotificationWhenWithinThreshold() {
        long threshold = TimeUnit.DAYS.toMillis(3);
        long lastUsed = System.currentTimeMillis() - threshold + TimeUnit.HOURS.toMillis(2);
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(lastUsed);

        AppUsageNotificationWorker worker = createWorker();

        worker.doWork();

        verify(sharedPreferences).getLong("lastUsed", 0);
        verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
        verify(notificationManager, never()).notify(anyInt(), any(Notification.class));

        ArgumentCaptor<Long> timestampCaptor = ArgumentCaptor.forClass(Long.class);
        verify(editor).putLong(eq("lastUsed"), timestampCaptor.capture());
        verify(editor).apply();

        long updatedTimestamp = timestampCaptor.getValue();
        assertTrue(updatedTimestamp > lastUsed);
    }

    @Test
    public void doWork_skipsNotificationWhenTimestampMissing() {
        when(sharedPreferences.getLong(eq("lastUsed"), anyLong())).thenReturn(0L);

        AppUsageNotificationWorker worker = createWorker();

        worker.doWork();

        verify(sharedPreferences).getLong("lastUsed", 0);
        verify(notificationManager, never()).createNotificationChannel(any(NotificationChannel.class));
        verify(notificationManager, never()).notify(anyInt(), any(Notification.class));

        ArgumentCaptor<Long> timestampCaptor = ArgumentCaptor.forClass(Long.class);
        verify(editor).putLong(eq("lastUsed"), timestampCaptor.capture());
        verify(editor).apply();

        long updatedTimestamp = timestampCaptor.getValue();
        assertTrue(updatedTimestamp > 0L);
    }

    private AppUsageNotificationWorker createWorker() {
        TestContext context = new TestContext(sharedPreferences, notificationManager, resources);
        return new AppUsageNotificationWorker(context, workerParameters, sharedPreferences, notificationManager);
    }

    private static class TestContext extends MockContext {

        private final SharedPreferences sharedPreferences;
        private final NotificationManager notificationManager;
        private final Resources resources;
        private final ApplicationInfo applicationInfo;

        TestContext(SharedPreferences sharedPreferences,
                    NotificationManager notificationManager,
                    Resources resources) {
            this.sharedPreferences = sharedPreferences;
            this.notificationManager = notificationManager;
            this.resources = resources;
            this.applicationInfo = new ApplicationInfo();
            this.applicationInfo.packageName = PACKAGE_NAME;
            this.applicationInfo.targetSdkVersion = Build.VERSION_CODES.O;
        }

        @Override
        public Context getApplicationContext() {
            return this;
        }

        @Override
        public SharedPreferences getSharedPreferences(String name, int mode) {
            return sharedPreferences;
        }

        @Override
        public Object getSystemService(String name) {
            if (Context.NOTIFICATION_SERVICE.equals(name)) {
                return notificationManager;
            }
            return super.getSystemService(name);
        }

        @Override
        public Resources getResources() {
            return resources;
        }

        @Override
        public String getPackageName() {
            return PACKAGE_NAME;
        }

        @Override
        public ApplicationInfo getApplicationInfo() {
            return applicationInfo;
        }

        @Override
        public String getOpPackageName() {
            return PACKAGE_NAME;
        }
    }
}
