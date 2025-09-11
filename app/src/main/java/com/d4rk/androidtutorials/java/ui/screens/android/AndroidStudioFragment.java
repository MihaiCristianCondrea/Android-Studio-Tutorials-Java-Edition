package com.d4rk.androidtutorials.java.ui.screens.android;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.preferences.NativeAdPreference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AndroidStudioFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_android_studio, rootKey);

        PreferenceScreen screen = getPreferenceScreen();
        if (screen == null) {
            return;
        }

        List<PreferenceCategory> categories = new ArrayList<>();
        for (int i = 0; i < screen.getPreferenceCount(); i++) {
            Preference pref = screen.getPreference(i);
            if (pref instanceof PreferenceCategory category) {
                categories.add(category);
            }
        }

        int lessonCount = 0;
        for (PreferenceCategory category : categories) {
            lessonCount += category.getPreferenceCount();
        }

        int adCount = lessonCount / 3;
        if (adCount <= 0) {
            return;
        }

        List<Integer> adPositions = new ArrayList<>();
        for (int i = 0; i < lessonCount; i++) {
            adPositions.add(i);
        }
        Collections.shuffle(adPositions, new Random());
        adPositions = adPositions.subList(0, adCount);
        Collections.sort(adPositions);

        int nextAd = 0;
        int current = 0;
        for (PreferenceCategory category : categories) {
            List<Preference> originals = new ArrayList<>();
            for (int i = 0; i < category.getPreferenceCount(); i++) {
                originals.add(category.getPreference(i));
            }
            category.removeAll();
            for (Preference lesson : originals) {
                if (nextAd < adPositions.size() && current == adPositions.get(nextAd)) {
                    category.addPreference(new NativeAdPreference(requireContext()));
                    nextAd++;
                }
                category.addPreference(lesson);
                current++;
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_android_studio, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_lessons_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPreferences(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPreferences(newText);
                return true;
            }
        });
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                filterPreferences("");
                return true;
            }
        });
    }

    private void filterPreferences(String query) {
        PreferenceScreen screen = getPreferenceScreen();
        if (screen == null) {
            return;
        }
        String lower = query == null ? "" : query.toLowerCase();
        for (int i = 0; i < screen.getPreferenceCount(); i++) {
            Preference pref = screen.getPreference(i);
            applyFilter(pref, lower);
        }
    }

    private boolean applyFilter(Preference pref, String query) {
        if (pref instanceof PreferenceCategory category) {
            boolean visible = false;
            for (int i = 0; i < category.getPreferenceCount(); i++) {
                Preference child = category.getPreference(i);
                boolean childVisible = applyFilter(child, query);
                visible |= childVisible;
            }
            category.setVisible(visible);
            return visible;
        } else {
            CharSequence title = pref.getTitle();
            if (title == null) {
                boolean visible = query.isEmpty();
                pref.setVisible(visible);
                return visible;
            }
            boolean matches = title.toString().toLowerCase().contains(query);
            pref.setVisible(matches);
            return matches;
        }
    }
}