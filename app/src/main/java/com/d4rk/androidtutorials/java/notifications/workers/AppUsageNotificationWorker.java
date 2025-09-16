package com.d4rk.androidtutorials.java.notifications.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.d4rk.androidtutorials.java.R;

/**
 * A {@link Worker} that checks the last time the app was used and displays a notification
 * if it exceeds a certain threshold.
 */
public class AppUsageNotificationWorker extends Worker {

    private final SharedPreferences sharedPreferences;
    @Nullable
    private final NotificationManager notificationManager;

    /**
     * Constructor for {@link AppUsageNotificationWorker}.
     *
     * @param context      The context of the application.
     * @param workerParams The parameters of the worker.
     */
    public AppUsageNotificationWorker(@NonNull Context context,
                                      @NonNull WorkerParameters workerParams) {
        this(context,
                workerParams,
                PreferenceManager.getDefaultSharedPreferences(context),
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
    }

    @VisibleForTesting
    AppUsageNotificationWorker(@NonNull Context context,
                               @NonNull WorkerParameters workerParams,
                               @NonNull SharedPreferences sharedPreferences,
                               @Nullable NotificationManager notificationManager) {
        super(context, workerParams);
        this.sharedPreferences = sharedPreferences;
        this.notificationManager = notificationManager;
    }

    /**
     * Performs the work of the worker, which involves checking the last used timestamp
     * and displaying a notification if necessary.
     *
     * @return The {@link Result} of the work.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        long currentTimestamp = System.currentTimeMillis();
        long notificationThreshold = 3 * 24 * 60 * 60 * 1000;
        long lastUsedTimestamp = sharedPreferences.getLong("lastUsed", 0);

        if (lastUsedTimestamp != 0 && notificationManager != null
                && currentTimestamp - lastUsedTimestamp > notificationThreshold) {
            String appUsageChannelId = "app_usage_channel";
            NotificationChannel appUsageChannel = new NotificationChannel(
                    appUsageChannelId,
                    getApplicationContext().getString(R.string.app_usage_notifications),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(appUsageChannel);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(getApplicationContext(),
                            appUsageChannelId)
                            .setSmallIcon(R.drawable.ic_notification_important)
                            .setContentTitle(getApplicationContext().getString(
                                    R.string.notification_last_time_used_title))
                            .setContentText(getApplicationContext().getString(
                                    R.string.summary_notification_last_time_used))
                            .setAutoCancel(true);
            int appUsageNotificationId = 0;
            notificationManager.notify(appUsageNotificationId,
                    notificationBuilder.build());
        }

        sharedPreferences.edit().putLong("lastUsed", currentTimestamp).apply();

        return Result.success();
    }
}