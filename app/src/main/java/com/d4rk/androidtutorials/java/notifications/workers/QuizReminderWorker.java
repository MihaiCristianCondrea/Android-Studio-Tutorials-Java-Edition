package com.d4rk.androidtutorials.java.notifications.workers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ui.screens.quiz.QuizActivity;

/**
 * Worker that displays the daily quiz reminder notification.
 */
public class QuizReminderWorker extends Worker {

    public QuizReminderWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Result doWork() {
        NotificationManager manager = (NotificationManager) getApplicationContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "quiz_reminder_channel";
        NotificationChannel channel = new NotificationChannel(
                channelId,
                getApplicationContext().getString(R.string.quiz_reminder_title),
                NotificationManager.IMPORTANCE_HIGH
        );
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_check_circle)
                .setContentTitle(getApplicationContext().getString(R.string.quiz_reminder_title))
                .setContentText(getApplicationContext().getString(R.string.quiz_reminder_body))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        manager.notify(1001, builder.build());
        return Result.success();
    }
}
