package com.d4rk.androidtutorials.java.notifications.receivers;

import android.content.Context;
import android.content.Intent;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.d4rk.androidtutorials.java.notifications.workers.AppUsageNotificationWorker;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

public class AppUsageNotificationReceiverTest {

    @Test
    public void onReceive_enqueuesOneTimeWorkRequestForAppUsageNotificationWorker() {
        Context context = mock(Context.class);
        Intent intent = mock(Intent.class);
        WorkManager workManager = mock(WorkManager.class);

        try (MockedStatic<WorkManager> mockedWorkManager = mockStatic(WorkManager.class)) {
            mockedWorkManager.when(() -> WorkManager.getInstance(context)).thenReturn(workManager);

            AppUsageNotificationReceiver receiver = new AppUsageNotificationReceiver();
            receiver.onReceive(context, intent);

            ArgumentCaptor<OneTimeWorkRequest> requestCaptor = ArgumentCaptor.forClass(OneTimeWorkRequest.class);
            verify(workManager).enqueue(requestCaptor.capture());

            OneTimeWorkRequest request = requestCaptor.getValue();
            assertEquals(AppUsageNotificationWorker.class.getName(),
                    request.getWorkSpec().workerClassName);
        }
    }
}
