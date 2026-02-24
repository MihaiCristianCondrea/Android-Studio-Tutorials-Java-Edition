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
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ItemPreferenceBinding;
import com.d4rk.androidtutorials.java.ui.components.dialogs.RequireRestartDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Nullable
    private RecyclerView settingsList;
    @Nullable
    private RecyclerView.AdapterDataObserver preferenceAdapterObserver;
    @Nullable
    private RecyclerView.OnChildAttachStateChangeListener preferenceChildAttachListener;
    @Nullable
    private ViewTreeObserver.OnGlobalLayoutListener preferenceLayoutListener;
    @NonNull
    private final List<PreferenceMetadata> preferenceMetadataCache = new ArrayList<>();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_settings, rootKey);
        SettingsActivity activity = (SettingsActivity) requireActivity();
        SettingsViewModel settingsViewModel = activity.getSettingsViewModel();
        setupLabelVisibilityPreference();
        setupConsentAnalyticsPreference(settingsViewModel);
        setupDefaultTabPreference();
        setupNotificationsPreference();
        setupDeviceInfoPreference();
        rebuildPreferenceMetadataCache();
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
        preferenceMetadataCache.clear();
        settingsList = null;
        super.onDestroyView();
    }

    private void setupPreferenceCardStyling(@NonNull RecyclerView listView) {
        Runnable refreshPreferenceCardsRunnable = () -> updatePreferenceCardShapes(listView);
        Runnable refreshDataRunnable = () -> {
            rebuildPreferenceMetadataCache();
            refreshPreferenceCardsRunnable.run();
        };
        RecyclerView.Adapter<?> adapter = listView.getAdapter();
        if (adapter != null) {
            preferenceAdapterObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    refreshDataRunnable.run();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    refreshDataRunnable.run();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    refreshDataRunnable.run();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    refreshDataRunnable.run();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    refreshDataRunnable.run();
                }
            };
            adapter.registerAdapterDataObserver(preferenceAdapterObserver);
        }
        preferenceChildAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                refreshPreferenceCardsRunnable.run();
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                refreshPreferenceCardsRunnable.run();
            }
        };
        listView.addOnChildAttachStateChangeListener(preferenceChildAttachListener);
        preferenceLayoutListener = refreshPreferenceCardsRunnable::run;
        listView.getViewTreeObserver().addOnGlobalLayoutListener(preferenceLayoutListener);
        listView.post(refreshDataRunnable);
    }

    private void updatePreferenceCardShapes(@NonNull RecyclerView listView) {
        RecyclerView.Adapter<?> adapter = listView.getAdapter();
        if (adapter == null) {
            return;
        }
        if (preferenceMetadataCache.isEmpty()) {
            return;
        }
        int itemCount = Math.min(adapter.getItemCount(), preferenceMetadataCache.size());
        for (int position = 0; position < itemCount; position++) {
            PreferenceMetadata metadata = preferenceMetadataCache.get(position);
            if (metadata.isCategory) {
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
            applyRoundedCorners((MaterialCardView) itemView,
                    metadata.isFirstInSection, metadata.isLastInSection);
            syncAccessoryVisibility(itemView);
        }
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
        ItemPreferenceBinding binding = ItemPreferenceBinding.bind(itemView);
        View icon = binding.icon;
        boolean showIcon = icon.getVisibility() == View.VISIBLE;
        icon.setVisibility(showIcon ? View.VISIBLE : View.GONE);
        View widgetFrame = binding.widgetFrame;
        if (widgetFrame instanceof ViewGroup widgetGroup) {
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

    @NonNull
    private List<Preference> getVisiblePreferences(@NonNull PreferenceGroup group) {
        List<Preference> preferences = new ArrayList<>();
        collectVisiblePreferences(group, preferences);
        return preferences;
    }

    private void rebuildPreferenceMetadataCache() {
        preferenceMetadataCache.clear();
        PreferenceScreen screen = getPreferenceScreen();
        if (screen == null) {
            return;
        }
        List<Preference> visiblePreferences = getVisiblePreferences(screen);
        int itemCount = visiblePreferences.size();
        for (int position = 0; position < itemCount; position++) {
            Preference preference = visiblePreferences.get(position);
            boolean isCategory = preference instanceof PreferenceCategory;
            boolean isFirstInSection = true;
            boolean isLastInSection = true;
            if (!isCategory) {
                Preference previousPreference = position > 0 ? visiblePreferences.get(position - 1) : null;
                Preference nextPreference = position + 1 < itemCount ? visiblePreferences.get(position + 1) : null;
                isFirstInSection = previousPreference == null
                        || previousPreference instanceof PreferenceCategory;
                isLastInSection = nextPreference == null
                        || nextPreference instanceof PreferenceCategory;
            }
            preferenceMetadataCache.add(new PreferenceMetadata(isCategory,
                    isFirstInSection, isLastInSection));
        }
    }

    private void collectVisiblePreferences(@NonNull PreferenceGroup group,
                                           @NonNull List<Preference> out) {
        for (int index = 0; index < group.getPreferenceCount(); index++) {
            Preference preference = group.getPreference(index);
            if (!preference.isVisible()) {
                continue;
            }
            out.add(preference);
            if (preference instanceof PreferenceGroup && !(preference instanceof PreferenceScreen)) {
                collectVisiblePreferences((PreferenceGroup) preference, out);
            }
        }
    }

    private class PreferenceSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        PreferenceSpacingDecoration(@NonNull Context context) {
            spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                    context.getResources().getDisplayMetrics());
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            if (position == RecyclerView.NO_POSITION) {
                return;
            }
            if (position >= preferenceMetadataCache.size()) {
                return;
            }
            PreferenceMetadata metadata = preferenceMetadataCache.get(position);
            if (!metadata.isCategory) {
                outRect.bottom = spacing;
            }
        }
    }

    private static class PreferenceMetadata {
        private final boolean isCategory;
        private final boolean isFirstInSection;
        private final boolean isLastInSection;

        PreferenceMetadata(boolean isCategory,
                           boolean isFirstInSection, boolean isLastInSection) {
            this.isCategory = isCategory;
            this.isFirstInSection = isFirstInSection;
            this.isLastInSection = isLastInSection;
        }
    }
}
