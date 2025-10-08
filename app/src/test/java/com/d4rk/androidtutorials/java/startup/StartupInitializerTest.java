package com.d4rk.androidtutorials.java.startup;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import android.content.Context;

import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

public class StartupInitializerTest {

    @Before
    public void setUp() {
        StartupInitializer.resetForTesting();
    }

    @After
    public void tearDown() {
        StartupInitializer.resetForTesting();
    }

    @Test
    public void schedule_enqueuesWorkWithKeepPolicy() {
        Context context = mock(Context.class);
        WorkManager workManager = mock(WorkManager.class);

        try (MockedStatic<WorkManager> mockedWorkManager = mockStatic(WorkManager.class)) {
            mockedWorkManager.when(() -> WorkManager.getInstance(context)).thenReturn(workManager);

            StartupInitializer.schedule(context);

            verify(workManager).enqueueUniqueWork(
                    eq("startup_init"),
                    eq(ExistingWorkPolicy.KEEP),
                    any(OneTimeWorkRequest.class)
            );
        }
    }

    @Test
    public void schedule_multipleCalls_enqueuesWorkOnlyOnce() {
        Context context = mock(Context.class);
        WorkManager workManager = mock(WorkManager.class);

        try (MockedStatic<WorkManager> mockedWorkManager = mockStatic(WorkManager.class)) {
            mockedWorkManager.when(() -> WorkManager.getInstance(context)).thenReturn(workManager);

            StartupInitializer.schedule(context);
            StartupInitializer.schedule(context);

            verify(workManager).enqueueUniqueWork(
                    eq("startup_init"),
                    eq(ExistingWorkPolicy.KEEP),
                    any(OneTimeWorkRequest.class)
            );
            verifyNoMoreInteractions(workManager);
        }
    }
}
