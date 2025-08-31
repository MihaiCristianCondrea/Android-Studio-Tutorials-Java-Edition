package com.d4rk.androidtutorials.java.ui.screens.android.lessons.notifications.inbox;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import androidx.core.app.NotificationCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityNotificationBinding;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;

public class InboxNotificationActivity extends UpNavigationActivity {
    private final String notificationChannelId = "inbox_notification";
    private final int notificationId = 1;
    private final Handler handler = new Handler();
    private ActivityNotificationBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.buttonShowNotification.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(notificationChannelId, "Inbox Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, notificationChannelId)
                    .setSmallIcon(R.drawable.ic_notification_important)
                    .setContentTitle("5 new messages")
                    .setStyle(new NotificationCompat.InboxStyle()
                            .addLine("Message 1")
                            .addLine("Message 2")
                            .setSummaryText("+3 more"))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            notificationManager.notify(notificationId, notificationBuilder.build());
        });
        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "InboxNotification");
            startActivity(intent);
        });
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
}