package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.d4rk.androidtutorials.java.notifications.receivers.AppUsageNotificationReceiver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AppUsageNotificationsManagerTest {

    private static final String PACKAGE_NAME = "com.d4rk.androidtutorials.java";

    @Mock
    private Context context;

    @Mock
    private AlarmManager alarmManager;

    private AutoCloseable closeable;

    @Before
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        when(context.getSystemService(Context.ALARM_SERVICE)).thenReturn(alarmManager);
        when(context.getPackageName()).thenReturn(PACKAGE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    public void scheduleAppUsageCheck_setsRepeatingAlarmEveryThreeDaysWithProperPendingIntent() {
        PendingIntent pendingIntent = mock(PendingIntent.class);

        try (MockedStatic<PendingIntent> pendingIntentStatic = mockStatic(PendingIntent.class)) {
            pendingIntentStatic.when(() -> PendingIntent.getBroadcast(
                    eq(context),
                    eq(0),
                    any(Intent.class),
                    eq(PendingIntent.FLAG_IMMUTABLE)
            )).thenReturn(pendingIntent);

            AppUsageNotificationsManager manager = new AppUsageNotificationsManager(context);

            long beforeCall = System.currentTimeMillis();
            manager.scheduleAppUsageCheck();
            long afterCall = System.currentTimeMillis();

            long expectedInterval = TimeUnit.DAYS.toMillis(3);

            ArgumentCaptor<Long> triggerTimeCaptor = ArgumentCaptor.forClass(Long.class);

            verify(alarmManager).setRepeating(
                    eq(AlarmManager.RTC_WAKEUP),
                    triggerTimeCaptor.capture(),
                    eq(expectedInterval),
                    eq(pendingIntent)
            );

            long triggerTime = triggerTimeCaptor.getValue();
            long scheduledBaseTime = triggerTime - expectedInterval;

            assertTrue("Trigger time should be scheduled three days from the invocation time", scheduledBaseTime >= beforeCall);
            assertTrue("Trigger time should be scheduled three days from the invocation time", scheduledBaseTime <= afterCall);

            pendingIntentStatic.verify(() -> PendingIntent.getBroadcast(
                    eq(context),
                    eq(0),
                    argThat(this::isAppUsageReceiverIntent),
                    eq(PendingIntent.FLAG_IMMUTABLE)
            ));
        }
    }

    private boolean isAppUsageReceiverIntent(Intent intent) {
        if (intent == null) {
            return false;
        }
        ComponentName componentName = intent.getComponent();
        return componentName != null
                && componentName.getClassName().equals(AppUsageNotificationReceiver.class.getName());
    }
}
