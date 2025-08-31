package com.d4rk.androidtutorials.java.ui.screens.android.lessons.notifications.simple;

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

public class SimpleNotificationActivity extends UpNavigationActivity {
    private final String simpleChannelId = "simple_notification";
    private final int simpleNotificationId = 1;
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
            NotificationChannel simpleChannel = new NotificationChannel(simpleChannelId, "Simple Notifications", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(simpleChannel);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, simpleChannelId)
                    .setSmallIcon(R.drawable.ic_notification_important)
                    .setContentTitle("Title")
                    .setContentText("Look! You've added notifications to your app!")
                    .setAutoCancel(true);
            notificationManager.notify(simpleNotificationId, notificationBuilder.build());
        });
        binding.floatingButtonShowSyntax.setOnClickListener(v -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "SimpleNotification");
            startActivity(intent);
        });
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }
}