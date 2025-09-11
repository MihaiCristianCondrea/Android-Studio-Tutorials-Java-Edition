package com.d4rk.androidtutorials.java.ui.screens.android;

import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.views.NativeAdBannerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AndroidStudioFragment extends Fragment {

    private static boolean mobileAdsInitialized = false;
    private final List<Lesson> allLessons = new ArrayList<>();
    private LessonsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_android_studio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureMobileAdsInitialized();
        RecyclerView list = view.findViewById(R.id.lessons_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LessonsAdapter();
        list.setAdapter(adapter);
        allLessons.clear();
        allLessons.addAll(loadLessons());
        populateAdapter(allLessons);
    }

    private void ensureMobileAdsInitialized() {
        if (!mobileAdsInitialized) {
            MobileAds.initialize(requireContext());
            mobileAdsInitialized = true;
        }
    }

    private List<Lesson> loadLessons() {
        List<Lesson> lessons = new ArrayList<>();
        XmlResourceParser parser = getResources().getXml(R.xml.preferences_android_studio);
        try {
            int event = parser.getEventType();
            Lesson current = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.endsWith("Preference") && !name.endsWith("PreferenceCategory")) {
                        current = new Lesson();
                        int titleRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "title", 0);
                        if (titleRes != 0) current.title = getString(titleRes);
                        int summaryRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "summary", 0);
                        if (summaryRes != 0) current.summary = getString(summaryRes);
                        current.iconRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "icon", 0);
                    } else if ("intent".equals(name) && current != null) {
                        Intent intent = new Intent();
                        String action = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "action");
                        if (action != null) intent.setAction(action);
                        String targetPackage = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "targetPackage");
                        String targetClass = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "targetClass");
                        if (targetPackage != null && targetClass != null) {
                            intent.setClassName(targetPackage, targetClass);
                        }
                        String data = parser.getAttributeValue("http://schemas.android.com/apk/res/android", "data");
                        if (data != null) intent.setData(Uri.parse(data));
                        current.intent = intent;
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    String name = parser.getName();
                    if (current != null && name.endsWith("Preference") && !name.endsWith("PreferenceCategory")) {
                        lessons.add(current);
                        current = null;
                    }
                }
                event = parser.next();
            }
        } catch (XmlPullParserException | IOException ignored) {
        } finally {
            parser.close();
        }
        return lessons;
    }

    private void populateAdapter(List<Lesson> lessons) {
        List<Object> items = new ArrayList<>();
        int adCount = lessons.size() / 3;
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < lessons.size(); i++) {
            positions.add(i);
        }
        Collections.shuffle(positions, new Random());
        positions = positions.subList(0, adCount);
        Collections.sort(positions);
        int adIndex = 0;
        for (int i = 0; i < lessons.size(); i++) {
            if (adIndex < positions.size() && i == positions.get(adIndex)) {
                items.add(new AdItem());
                adIndex++;
            }
            items.add(lessons.get(i));
        }
        adapter.setItems(items);
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
                filterLessons(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterLessons(newText);
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
                filterLessons("");
                return true;
            }
        });
    }

    private void filterLessons(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        List<Lesson> filtered = new ArrayList<>();
        for (Lesson l : allLessons) {
            if (l.title != null && l.title.toLowerCase().contains(lower)) {
                filtered.add(l);
            }
        }
        populateAdapter(filtered);
    }

    private static class AdItem {}

    private static class Lesson {
        String title;
        String summary;
        int iconRes;
        Intent intent;
    }

    private static class LessonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_LESSON = 0;
        private static final int TYPE_AD = 1;
        private final List<Object> items = new ArrayList<>();

        void setItems(List<Object> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            Object item = items.get(position);
            return item instanceof Lesson ? TYPE_LESSON : TYPE_AD;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == TYPE_AD) {
                NativeAdBannerView adView = new NativeAdBannerView(parent.getContext());
                adView.setLayoutParams(new RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                adView.setNativeAdLayout(R.layout.android_studio_list_native_ad);
                return new AdHolder(adView);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.android_studio_lesson_item, parent, false);
                return new LessonHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (getItemViewType(position) == TYPE_AD) {
                AdHolder adHolder = (AdHolder) holder;
                adHolder.adView.loadAd(new AdRequest.Builder().build(), new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        adHolder.adView.setVisibility(View.GONE);
                    }
                });
            } else {
                Lesson lesson = (Lesson) items.get(position);
                ((LessonHolder) holder).bind(lesson);
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class AdHolder extends RecyclerView.ViewHolder {
            final NativeAdBannerView adView;
            AdHolder(@NonNull NativeAdBannerView itemView) {
                super(itemView);
                this.adView = itemView;
            }
        }

        static class LessonHolder extends RecyclerView.ViewHolder {
            final ImageView icon;
            final TextView title;
            final TextView summary;

            LessonHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.lesson_icon);
                title = itemView.findViewById(R.id.lesson_title);
                summary = itemView.findViewById(R.id.lesson_summary);
            }

            void bind(Lesson lesson) {
                if (lesson.iconRes != 0) {
                    icon.setImageResource(lesson.iconRes);
                    icon.setVisibility(View.VISIBLE);
                } else {
                    icon.setVisibility(View.GONE);
                }
                title.setText(lesson.title);
                if (lesson.summary != null) {
                    summary.setText(lesson.summary);
                    summary.setVisibility(View.VISIBLE);
                } else {
                    summary.setVisibility(View.GONE);
                }
                itemView.setOnClickListener(v -> {
                    if (lesson.intent != null) {
                        v.getContext().startActivity(lesson.intent);
                    }
                });
            }
        }
    }
}
