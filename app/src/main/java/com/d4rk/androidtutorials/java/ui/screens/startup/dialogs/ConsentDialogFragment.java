package com.d4rk.androidtutorials.java.ui.screens.startup.dialogs;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.d4rk.androidtutorials.java.BuildConfig;
import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.DialogConsentBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class ConsentDialogFragment extends DialogFragment {

    private ConsentListener listener;

    public void setConsentListener(ConsentListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        DialogConsentBinding binding = DialogConsentBinding.inflate(getLayoutInflater());

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(requireContext());
        boolean defaultChecked = !BuildConfig.DEBUG;

        // Cache preference keys early so the dialog can still operate safely if the
        // fragment gets detached before the positive button callback runs.
        String keyAnalytics = getString(R.string.key_consent_analytics);
        String keyAdStorage = getString(R.string.key_consent_ad_storage);
        String keyAdUserData = getString(R.string.key_consent_ad_user_data);
        String keyAdPersonalization = getString(R.string.key_consent_ad_personalization);

        boolean analytics = prefs.getBoolean(keyAnalytics, defaultChecked);
        boolean adStorage = prefs.getBoolean(keyAdStorage, defaultChecked);
        boolean adUserData = prefs.getBoolean(keyAdUserData, defaultChecked);
        boolean adPersonalization = prefs.getBoolean(keyAdPersonalization, defaultChecked);

        binding.checkAnalyticsStorage.setChecked(analytics);
        binding.checkAdStorage.setChecked(adStorage);
        binding.checkAdUserData.setChecked(adUserData);
        binding.checkAdPersonalization.setChecked(adPersonalization);

        setCancelable(false);

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.consent_dialog_title)
                .setView(binding.getRoot())
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    boolean a = binding.checkAnalyticsStorage.isChecked();
                    boolean b = binding.checkAdStorage.isChecked();
                    boolean c = binding.checkAdUserData.isChecked();
                    boolean d = binding.checkAdPersonalization.isChecked();

                    prefs.edit()
                            .putBoolean(keyAnalytics, a)
                            .putBoolean(keyAdStorage, b)
                            .putBoolean(keyAdUserData, c)
                            .putBoolean(keyAdPersonalization, d)
                            .apply();

                    if (listener != null) {
                        listener.onConsentSet(a, b, c, d);
                    }
                })
                .create();
    }

    public interface ConsentListener {
        void onConsentSet(boolean analytics, boolean adStorage, boolean adUserData, boolean adPersonalization);
    }
}
