package com.d4rk.androidtutorials.java.startup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

/**
 * Helper used to enqueue a one-off background job for initializing ads and
 * cookies. The work is only scheduled once per app launch sequence.
 */
public final class StartupInitializer {

    private static final String WORK_NAME = "startup_init";

    private StartupInitializer() {
        // no-op
    }

    /**
     * Schedules the {@link StartupWorker} using {@link WorkManager}. If the work
     * is already enqueued, this call is ignored.
     */
    public static void schedule(@NonNull Context context) {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(
                StartupWorker.class).build();
        WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME, ExistingWorkPolicy.KEEP, workRequest);
    }
}

