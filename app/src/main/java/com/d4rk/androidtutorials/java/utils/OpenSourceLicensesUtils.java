package com.d4rk.androidtutorials.java.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.R;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

public final class OpenSourceLicensesUtils {

    private OpenSourceLicensesUtils() {
        // Utility class
    }

    public static void openLicensesScreen(@Nullable Context context) {
        if (context == null) {
            return;
        }

        OssLicensesMenuActivity.setActivityTitle(context.getString(R.string.open_source_licenses));

        Intent intent = new Intent(context, OssLicensesMenuActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        context.startActivity(intent);
    }
}