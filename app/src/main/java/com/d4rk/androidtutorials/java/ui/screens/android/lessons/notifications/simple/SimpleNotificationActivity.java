package com.d4rk.androidtutorials.java.ui.screens.android.lessons.notifications.simple;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityNotificationBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class SimpleNotificationActivity extends SyntaxFabActivity {
    private final String simpleChannelId = "simple_notification";
    private final int simpleNotificationId = 1;
    private ActivityNotificationBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
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
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "SimpleNotification");
            startActivity(intent);
        });
    }
}
