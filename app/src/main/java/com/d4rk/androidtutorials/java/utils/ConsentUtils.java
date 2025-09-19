package com.d4rk.androidtutorials.java.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.EnumMap;
import java.util.Map;

public class ConsentUtils {

    public static void applyStoredConsent(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean analytics = prefs.getBoolean(context.getString(R.string.key_consent_analytics), true);
        boolean adStorage = prefs.getBoolean(context.getString(R.string.key_consent_ad_storage), true);
        boolean adUserData = prefs.getBoolean(context.getString(R.string.key_consent_ad_user_data), true);
        boolean adPersonalization = prefs.getBoolean(context.getString(R.string.key_consent_ad_personalization), true);
        updateFirebaseConsent(context, analytics, adStorage, adUserData, adPersonalization);
    }

    public static void updateFirebaseConsent(Context context,
                                             boolean analytics,
                                             boolean adStorage,
                                             boolean adUserData,
                                             boolean adPersonalization) {
        Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> consentMap =
                new EnumMap<>(FirebaseAnalytics.ConsentType.class);
        consentMap.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE,
                analytics ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        consentMap.put(FirebaseAnalytics.ConsentType.AD_STORAGE,
                adStorage ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        consentMap.put(FirebaseAnalytics.ConsentType.AD_USER_DATA,
                adUserData ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        consentMap.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION,
                adPersonalization ? FirebaseAnalytics.ConsentStatus.GRANTED : FirebaseAnalytics.ConsentStatus.DENIED);
        FirebaseAnalytics.getInstance(context).setConsent(consentMap);
    }

    public static boolean canShowPersonalizedAds(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(context.getString(R.string.key_consent_ad_storage), true);
    }
}
