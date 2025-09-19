package com.d4rk.androidtutorials.java.utils;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.EnumMap;
import java.util.Map;

public class ConsentUtilsTest {

    @Test
    public void applyStoredConsent_readsPreferencesAndUpdatesFirebase() {
        Context context = mock(Context.class);
        SharedPreferences prefs = mock(SharedPreferences.class);
        FirebaseAnalytics analytics = mock(FirebaseAnalytics.class);

        when(context.getString(R.string.key_consent_analytics)).thenReturn("consent_analytics");
        when(context.getString(R.string.key_consent_ad_storage)).thenReturn("consent_ad_storage");
        when(context.getString(R.string.key_consent_ad_user_data)).thenReturn("consent_ad_user_data");
        when(context.getString(R.string.key_consent_ad_personalization)).thenReturn("consent_ad_personalization");

        when(prefs.getBoolean("consent_analytics", true)).thenReturn(false);
        when(prefs.getBoolean("consent_ad_storage", true)).thenReturn(true);
        when(prefs.getBoolean("consent_ad_user_data", true)).thenReturn(false);
        when(prefs.getBoolean("consent_ad_personalization", true)).thenReturn(true);

        try (MockedStatic<PreferenceManager> prefsStatic = Mockito.mockStatic(PreferenceManager.class);
             MockedStatic<FirebaseAnalytics> firebaseStatic = Mockito.mockStatic(FirebaseAnalytics.class)) {
            prefsStatic.when(() -> PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(prefs);
            firebaseStatic.when(() -> FirebaseAnalytics.getInstance(context)).thenReturn(analytics);

            ConsentUtils.applyStoredConsent(context);

            verify(prefs).getBoolean("consent_analytics", true);
            verify(prefs).getBoolean("consent_ad_storage", true);
            verify(prefs).getBoolean("consent_ad_user_data", true);
            verify(prefs).getBoolean("consent_ad_personalization", true);

            Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> expectedConsent =
                    new EnumMap<>(FirebaseAnalytics.ConsentType.class);
            expectedConsent.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE,
                    FirebaseAnalytics.ConsentStatus.DENIED);
            expectedConsent.put(FirebaseAnalytics.ConsentType.AD_STORAGE,
                    FirebaseAnalytics.ConsentStatus.GRANTED);
            expectedConsent.put(FirebaseAnalytics.ConsentType.AD_USER_DATA,
                    FirebaseAnalytics.ConsentStatus.DENIED);
            expectedConsent.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION,
                    FirebaseAnalytics.ConsentStatus.GRANTED);

            verify(analytics).setConsent(expectedConsent);
        }
    }

    @Test
    public void updateFirebaseConsent_setsExpectedStatuses() {
        Context context = mock(Context.class);
        FirebaseAnalytics analytics = mock(FirebaseAnalytics.class);

        try (MockedStatic<FirebaseAnalytics> firebaseStatic = Mockito.mockStatic(FirebaseAnalytics.class)) {
            firebaseStatic.when(() -> FirebaseAnalytics.getInstance(context)).thenReturn(analytics);

            boolean analyticsConsent = true;
            boolean adStorageConsent = false;
            boolean adUserDataConsent = true;
            boolean adPersonalizationConsent = false;

            ConsentUtils.updateFirebaseConsent(context,
                    analyticsConsent,
                    adStorageConsent,
                    adUserDataConsent,
                    adPersonalizationConsent);

            Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> expectedConsent =
                    new EnumMap<>(FirebaseAnalytics.ConsentType.class);
            expectedConsent.put(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE,
                    FirebaseAnalytics.ConsentStatus.GRANTED);
            expectedConsent.put(FirebaseAnalytics.ConsentType.AD_STORAGE,
                    FirebaseAnalytics.ConsentStatus.DENIED);
            expectedConsent.put(FirebaseAnalytics.ConsentType.AD_USER_DATA,
                    FirebaseAnalytics.ConsentStatus.GRANTED);
            expectedConsent.put(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION,
                    FirebaseAnalytics.ConsentStatus.DENIED);

            verify(analytics).setConsent(expectedConsent);
        }
    }

}
