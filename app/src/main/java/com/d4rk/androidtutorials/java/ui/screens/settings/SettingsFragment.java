package com.d4rk.androidtutorials.java.ui.screens.settings;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ui.components.dialogs.RequireRestartDialog;
import com.d4rk.androidtutorials.java.utils.OpenSourceLicensesUtils;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Nullable
    private RecyclerView settingsList;
    @Nullable
    private RecyclerView.AdapterDataObserver preferenceAdapterObserver;
    @Nullable
    private RecyclerView.OnChildAttachStateChangeListener preferenceChildAttachListener;
    @Nullable
    private ViewTreeObserver.OnGlobalLayoutListener preferenceLayoutListener;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        SettingsActivity activity = (SettingsActivity) requireActivity();
        SettingsViewModel settingsViewModel = activity.getSettingsViewModel();
        setupLabelVisibilityPreference();
        setupConsentAnalyticsPreference(settingsViewModel);
        setupDefaultTabPreference();
        setupOpenSourceLicensesPreference();
        setupNotificationsPreference();
        setupDeviceInfoPreference();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDivider(null);
        setDividerHeight(0);
        settingsList = getListView();
        RecyclerView listView = settingsList;
        int verticalPadding = getResources().getDimensionPixelSize(R.dimen.preference_list_vertical_padding);
        listView.setPadding(listView.getPaddingLeft(), verticalPadding,
                listView.getPaddingRight(), verticalPadding);
        listView.setClipToPadding(false);
        listView.addItemDecoration(new PreferenceSpacingDecoration(requireContext()));
        setupPreferenceCardStyling(listView);
    }

    private void setupLabelVisibilityPreference() {
        ListPreference labelVisibilityMode =
                findPreference(getString(R.string.key_bottom_navigation_bar_labels));
        if (labelVisibilityMode != null) {
            labelVisibilityMode.setOnPreferenceChangeListener((preference, newValue) -> {
                RequireRestartDialog restartDialog = new RequireRestartDialog();
                restartDialog.show(getChildFragmentManager(), RequireRestartDialog.class.getName());
                return true;
            });
        }
    }

    private void setupConsentAnalyticsPreference(@NonNull SettingsViewModel settingsViewModel) {
        SwitchPreferenceCompat consentAnalyticsPreference =
                findPreference(getString(R.string.key_consent_analytics));
        if (consentAnalyticsPreference != null) {
            consentAnalyticsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
                if (newValue instanceof Boolean) {
                    settingsViewModel.setConsentAccepted((Boolean) newValue);
                }
                return true;
            });
        }
    }

    private void setupDefaultTabPreference() {
        ListPreference defaultTab = findPreference(getString(R.string.key_default_tab));
        if (defaultTab != null) {
            defaultTab.setOnPreferenceChangeListener((preference, newValue) -> {
                RequireRestartDialog restartDialog = new RequireRestartDialog();
                restartDialog.show(getChildFragmentManager(), RequireRestartDialog.class.getName());
                return true;
            });
        }
    }

    private void setupOpenSourceLicensesPreference() {
        Preference ossPreference = findPreference(getString(R.string.key_open_source_licenses));
        if (ossPreference != null) {
            ossPreference.setOnPreferenceClickListener(preference -> {
                OpenSourceLicensesUtils.openLicensesScreen(requireContext());
                return true;
            });
        }
    }

    private void setupNotificationsPreference() {
        Preference notificationsSettings = findPreference(getString(R.string.key_notifications_settings));
        if (notificationsSettings != null) {
            notificationsSettings.setOnPreferenceClickListener(preference -> {
                Context context = getContext();
                if (context == null) {
                    return false;
                }
                Intent intent;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                } else {
                    intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                    intent.setData(uri);
                }
                startActivity(intent);
                return true;
            });
        }
    }

    private void setupDeviceInfoPreference() {
        Preference deviceInfoPreference = findPreference(getString(R.string.key_device_info));
        if (deviceInfoPreference == null) {
            return;
        }
        String version = buildDeviceInfoSummary();
        deviceInfoPreference.setSummary(version);
        deviceInfoPreference.setOnPreferenceClickListener(preference -> {
            ClipboardManager clipboard =
                    (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", version);
            clipboard.setPrimaryClip(clip);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
                Toast.makeText(getContext(), R.string.snack_copied_to_clipboard,
                        Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    @NonNull
    private String buildDeviceInfoSummary() {
        return String.format(getResources().getString(R.string.app_build),
                String.format("%s %s", getResources().getString(R.string.manufacturer),
                        Build.MANUFACTURER),
                String.format("%s %s", getResources().getString(R.string.device_model), Build.MODEL),
                String.format("%s %s", getResources().getString(R.string.android_version),
                        Build.VERSION.RELEASE),
                String.format("%s %s", getResources().getString(R.string.api_level), Build.VERSION.SDK_INT),
                String.format("%s %s", getResources().getString(R.string.arch),
                        TextUtils.join(",", Build.SUPPORTED_ABIS)));
    }

    @Override
    public void onDestroyView() {
        if (settingsList != null) {
            if (settingsList.getAdapter() != null && preferenceAdapterObserver != null) {
                settingsList.getAdapter().unregisterAdapterDataObserver(preferenceAdapterObserver);
            }
            if (preferenceChildAttachListener != null) {
                settingsList.removeOnChildAttachStateChangeListener(preferenceChildAttachListener);
            }
            if (preferenceLayoutListener != null) {
                ViewTreeObserver observer = settingsList.getViewTreeObserver();
                if (observer.isAlive()) {
                    observer.removeOnGlobalLayoutListener(preferenceLayoutListener);
                }
            }
        }
        preferenceAdapterObserver = null;
        preferenceChildAttachListener = null;
        preferenceLayoutListener = null;
        settingsList = null;
        super.onDestroyView();
    }

    private void setupPreferenceCardStyling(@NonNull RecyclerView listView) {
        Runnable updateRunnable = () -> updatePreferenceCardShapes(listView);
        RecyclerView.Adapter<?> adapter = listView.getAdapter();
        if (adapter != null) {
            preferenceAdapterObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    updateRunnable.run();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    updateRunnable.run();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    updateRunnable.run();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    updateRunnable.run();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    updateRunnable.run();
                }
            };
            adapter.registerAdapterDataObserver(preferenceAdapterObserver);
        }
        preferenceChildAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                updateRunnable.run();
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                updateRunnable.run();
            }
        };
        listView.addOnChildAttachStateChangeListener(preferenceChildAttachListener);
        preferenceLayoutListener = updateRunnable::run;
        listView.getViewTreeObserver().addOnGlobalLayoutListener(preferenceLayoutListener);
        listView.post(updateRunnable);
    }

    private void updatePreferenceCardShapes(@NonNull RecyclerView listView) {
        RecyclerView.Adapter<?> adapter = listView.getAdapter();
        if (!(adapter instanceof PreferenceGroupAdapter)) {
            return;
        }
        PreferenceGroupAdapter preferenceAdapter = (PreferenceGroupAdapter) adapter;
        int itemCount = preferenceAdapter.getItemCount();
        for (int position = 0; position < itemCount; position++) {
            Preference preference = preferenceAdapter.getItem(position);
            if (preference instanceof PreferenceCategory) {
                continue;
            }
            RecyclerView.ViewHolder holder = listView.findViewHolderForAdapterPosition(position);
            if (holder == null) {
                continue;
            }
            View itemView = holder.itemView;
            if (!(itemView instanceof MaterialCardView)) {
                continue;
            }
            boolean first = isFirstPreferenceInSection(preferenceAdapter, position);
            boolean last = isLastPreferenceInSection(preferenceAdapter, position);
            applyRoundedCorners((MaterialCardView) itemView, first, last);
            syncAccessoryVisibility(itemView);
        }
    }

    private boolean isFirstPreferenceInSection(@NonNull PreferenceGroupAdapter adapter, int position) {
        for (int index = position - 1; index >= 0; index--) {
            Preference previous = adapter.getItem(index);
            if (!previous.isVisible()) {
                continue;
            }
            if (previous instanceof PreferenceCategory) {
                return true;
            }
            return false;
        }
        return true;
    }

    private boolean isLastPreferenceInSection(@NonNull PreferenceGroupAdapter adapter, int position) {
        int itemCount = adapter.getItemCount();
        for (int index = position + 1; index < itemCount; index++) {
            Preference next = adapter.getItem(index);
            if (!next.isVisible()) {
                continue;
            }
            if (next instanceof PreferenceCategory) {
                return true;
            }
            return false;
        }
        return true;
    }

    private void applyRoundedCorners(@NonNull MaterialCardView card, boolean first, boolean last) {
        float dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                card.getResources().getDisplayMetrics());
        float dp24 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                card.getResources().getDisplayMetrics());
        ShapeAppearanceModel shapeAppearanceModel = card.getShapeAppearanceModel().toBuilder()
                .setTopLeftCorner(CornerFamily.ROUNDED, first ? dp24 : dp4)
                .setTopRightCorner(CornerFamily.ROUNDED, first ? dp24 : dp4)
                .setBottomLeftCorner(CornerFamily.ROUNDED, last ? dp24 : dp4)
                .setBottomRightCorner(CornerFamily.ROUNDED, last ? dp24 : dp4)
                .build();
        card.setShapeAppearanceModel(shapeAppearanceModel);
    }

    private void syncAccessoryVisibility(@NonNull View itemView) {
        View icon = itemView.findViewById(android.R.id.icon);
        View iconFrame = itemView.findViewById(android.R.id.icon_frame);
        if (iconFrame != null) {
            boolean showIcon = icon != null && icon.getVisibility() == View.VISIBLE;
            iconFrame.setVisibility(showIcon ? View.VISIBLE : View.GONE);
        }
        View widgetFrame = itemView.findViewById(android.R.id.widget_frame);
        if (widgetFrame instanceof ViewGroup) {
            ViewGroup widgetGroup = (ViewGroup) widgetFrame;
            boolean hasChild = widgetGroup.getChildCount() > 0;
            widgetFrame.setVisibility(hasChild ? View.VISIBLE : View.GONE);
            if (hasChild) {
                for (int index = 0; index < widgetGroup.getChildCount(); index++) {
                    View child = widgetGroup.getChildAt(index);
                    child.setDuplicateParentStateEnabled(true);
                }
            }
        }
    }

    private static class PreferenceSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        PreferenceSpacingDecoration(@NonNull Context context) {
            spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                    context.getResources().getDisplayMetrics());
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            RecyclerView.Adapter<?> adapter = parent.getAdapter();
            if (!(adapter instanceof PreferenceGroupAdapter)) {
                return;
            }
            int position = parent.getChildAdapterPosition(view);
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            Preference preference = ((PreferenceGroupAdapter) adapter).getItem(position);
            if (!(preference instanceof PreferenceCategory)) {
                outRect.bottom = spacing;
            }
        }
    }
}
