package com.d4rk.androidtutorials.java.notifications.workers;

import static org.junit.Assert.assertEquals;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.test.core.app.ApplicationProvider;
import androidx.preference.PreferenceManager;
import androidx.work.ListenableWorker;
import androidx.work.testing.TestListenableWorkerBuilder;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotificationManager;
import org.robolectric.shadows.ShadowSystemClock;
import org.robolectric.Shadows;

import java.util.concurrent.TimeUnit;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = Build.VERSION_CODES.O)
public class AppUsageNotificationWorkerTest {

    @Test
    public void doWork_postsNotificationAndUpdatesTimestamp() {
        Context context = ApplicationProvider.getApplicationContext();
        long now = 10_000L;
        ShadowSystemClock.setCurrentTimeMillis(now);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putLong("lastUsed", now - TimeUnit.DAYS.toMillis(4)).apply();

        AppUsageNotificationWorker worker = TestListenableWorkerBuilder.from(context, AppUsageNotificationWorker.class).build();
        ListenableWorker.Result result = worker.doWork();
        assertEquals(ListenableWorker.Result.success(), result);

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ShadowNotificationManager shadowNm = Shadows.shadowOf(nm);
        assertEquals(1, shadowNm.getAllNotifications().size());

        assertEquals(now, prefs.getLong("lastUsed", 0));
    }
}
