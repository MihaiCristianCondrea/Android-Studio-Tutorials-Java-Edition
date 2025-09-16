package com.d4rk.androidtutorials.java.notifications.managers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import java.util.function.IntSupplier;

/**
 * Utility class for managing app update notifications.
 *
 * <p>This class provides functionality to check for available app updates and send update
 * notifications with a deep link to the Play Store for user interaction.
 */
public class AppUpdateNotificationsManager {

    private final Context context;
    private final IntSupplier sdkIntSupplier;
    private final String updateChannelId = "update_channel";
    private final int updateNotificationId = 0;

    public AppUpdateNotificationsManager(Context context) {
        this(context, () -> Build.VERSION.SDK_INT);
    }

    AppUpdateNotificationsManager(Context context, IntSupplier sdkIntSupplier) {
        this.context = context;
        this.sdkIntSupplier = sdkIntSupplier;
    }

    /**
     * Checks for available app updates and sends a notification if a flexible update is available.
     *
     * <p>This method utilizes the AppUpdateManager to retrieve app update information.
     * If a flexible update is available, a notification is displayed to the user, prompting them
     * to update the app via the Play Store. The notification includes a deep link to the app's
     * Play Store listing.
     */
    public void checkAndSendUpdateNotification() {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(
                appUpdateInfo -> {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                            appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                        if (sdkIntSupplier.getAsInt() >= Build.VERSION_CODES.O) {
                            NotificationChannel updateChannel = new NotificationChannel(
                                    updateChannelId,
                                    context.getString(R.string.update_notifications),
                                    NotificationManager.IMPORTANCE_HIGH
                            );
                            notificationManager.createNotificationChannel(updateChannel);
                        }
                        NotificationCompat.Builder updateBuilder = new NotificationCompat.Builder(context, updateChannelId)
                                .setSmallIcon(R.drawable.ic_notification_update)
                                .setContentTitle(context.getString(R.string.notification_update_title))
                                .setContentText(context.getString(R.string.summary_notification_update))
                                .setAutoCancel(true)
                                .setContentIntent(
                                        PendingIntent.getActivity(
                                                context,
                                                0,
                                                new Intent(
                                                        Intent.ACTION_VIEW,
                                                        Uri.parse("market://details?id=" + context.getPackageName())
                                                ),
                                                PendingIntent.FLAG_IMMUTABLE
                                        )
                                );
                        notificationManager.notify(updateNotificationId, updateBuilder.build());
                    }
                }
        );
    }
}