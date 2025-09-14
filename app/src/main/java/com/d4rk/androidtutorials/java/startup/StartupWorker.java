package com.d4rk.androidtutorials.java.startup;

import android.content.Context;
import android.webkit.CookieManager;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.d4rk.androidtutorials.java.ads.AdUtils;

/**
 * Background worker that initializes advertising utilities and the
 * {@link CookieManager} away from the app's startup path.
 */
public class StartupWorker extends Worker {

    public StartupWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        AdUtils.initialize(getApplicationContext());
        CookieManager.getInstance();
        return Result.success();
    }
}

