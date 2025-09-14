package com.d4rk.androidtutorials.java.notifications.receivers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 26)
public class AppUsageNotificationReceiverTest {

    @Test
    public void onReceive_enqueuesWork() {
        Context context = ApplicationProvider.getApplicationContext();
        WorkManager mockManager = mock(WorkManager.class);

        try (MockedStatic<WorkManager> mockedStatic = Mockito.mockStatic(WorkManager.class)) {
            mockedStatic.when(() -> WorkManager.getInstance(context)).thenReturn(mockManager);

            AppUsageNotificationReceiver receiver = new AppUsageNotificationReceiver();
            receiver.onReceive(context, new Intent());

            verify(mockManager).enqueue(any(OneTimeWorkRequest.class));
        }
    }
}
