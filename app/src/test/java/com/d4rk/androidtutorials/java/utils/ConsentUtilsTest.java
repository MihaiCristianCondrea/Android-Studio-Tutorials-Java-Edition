package com.d4rk.androidtutorials.java.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.Map;

public class ConsentUtilsTest {

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

            ArgumentCaptor<Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>> captor =
                    ArgumentCaptor.forClass(Map.class);
            verify(analytics).setConsent(captor.capture());
            Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus> result = captor.getValue();

            assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED,
                    result.get(FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE));
            assertEquals(FirebaseAnalytics.ConsentStatus.DENIED,
                    result.get(FirebaseAnalytics.ConsentType.AD_STORAGE));
            assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED,
                    result.get(FirebaseAnalytics.ConsentType.AD_USER_DATA));
            assertEquals(FirebaseAnalytics.ConsentStatus.DENIED,
                    result.get(FirebaseAnalytics.ConsentType.AD_PERSONALIZATION));
        }
    }

    @Test
    public void canShowAds_readsStoredPreference() {
        Context context = mock(Context.class);
        SharedPreferences prefs = mock(SharedPreferences.class);
        when(context.getString(R.string.key_consent_ad_storage)).thenReturn("consent_ad_storage");

        try (MockedStatic<PreferenceManager> prefsStatic = Mockito.mockStatic(PreferenceManager.class)) {
            prefsStatic.when(() -> PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(prefs);

            when(prefs.getBoolean("consent_ad_storage", true)).thenReturn(false);
            assertFalse(ConsentUtils.canShowAds(context));

            when(prefs.getBoolean("consent_ad_storage", true)).thenReturn(true);
            assertTrue(ConsentUtils.canShowAds(context));
        }
    }
}
