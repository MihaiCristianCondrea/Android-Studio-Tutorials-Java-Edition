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
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AndroidStudioFragment extends Fragment {

    private static boolean mobileAdsInitialized = false;
    private final List<Object> allItems = new ArrayList<>();
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
        allItems.clear();
        allItems.addAll(loadItems());
        populateAdapter(allItems);
    }

    private void ensureMobileAdsInitialized() {
        if (!mobileAdsInitialized) {
            MobileAds.initialize(requireContext());
            mobileAdsInitialized = true;
        }
    }

    private List<Object> loadItems() {
        List<Object> items = new ArrayList<>();
        XmlResourceParser parser = getResources().getXml(R.xml.preferences_android_studio);
        try {
            int event = parser.getEventType();
            Lesson currentLesson = null;
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    String name = parser.getName();
                    if (name.endsWith("PreferenceCategory")) {
                        Category category = new Category();
                        int titleRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "title", 0);
                        if (titleRes != 0) category.title = getString(titleRes);
                        category.iconRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "icon", 0);
                        items.add(category);
                    } else if (name.endsWith("Preference") && !name.endsWith("PreferenceCategory")) {
                        currentLesson = new Lesson();
                        int titleRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "title", 0);
                        if (titleRes != 0) currentLesson.title = getString(titleRes);
                        int summaryRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "summary", 0);
                        if (summaryRes != 0) currentLesson.summary = getString(summaryRes);
                        currentLesson.iconRes = parser.getAttributeResourceValue("http://schemas.android.com/apk/res-auto", "icon", 0);
                    } else if ("intent".equals(name) && currentLesson != null) {
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
                        currentLesson.intent = intent;
                    }
                } else if (event == XmlPullParser.END_TAG) {
                    String name = parser.getName();
                    if (currentLesson != null && name.endsWith("Preference") && !name.endsWith("PreferenceCategory")) {
                        items.add(currentLesson);
                        currentLesson = null;
                    }
                }
                event = parser.next();
            }
        } catch (XmlPullParserException | IOException ignored) {
        } finally {
            parser.close();
        }
        return items;
    }

    private void populateAdapter(List<Object> source) {
        List<Object> items = new ArrayList<>();
        int lessonCount = 0;
        for (Object item : source) {
            if (item instanceof Lesson) {
                lessonCount++;
            }
        }
        int adCount = lessonCount / 3;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < lessonCount; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, new Random());
        indices = indices.subList(0, adCount);
        Collections.sort(indices);
        Set<Integer> adPositions = new HashSet<>(indices);
        int lessonIndex = 0;
        for (Object item : source) {
            if (item instanceof Lesson) {
                if (adPositions.contains(lessonIndex)) {
                    items.add(new AdItem());
                }
                items.add(item);
                lessonIndex++;
            } else {
                items.add(item);
            }
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
        if (lower.isEmpty()) {
            populateAdapter(allItems);
            return;
        }
        List<Object> filtered = new ArrayList<>();
        Category lastCategory = null;
        boolean categoryAdded = false;
        for (Object item : allItems) {
            if (item instanceof Category) {
                lastCategory = (Category) item;
                categoryAdded = false;
            } else if (item instanceof Lesson) {
                Lesson l = (Lesson) item;
                if (l.title != null && l.title.toLowerCase().contains(lower)) {
                    if (lastCategory != null && !categoryAdded) {
                        filtered.add(lastCategory);
                        categoryAdded = true;
                    }
                    filtered.add(l);
                }
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

    private static class Category {
        String title;
        int iconRes;
    }

    private static class LessonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private static final int TYPE_LESSON = 0;
        private static final int TYPE_AD = 1;
        private static final int TYPE_CATEGORY = 2;
        private final List<Object> items = new ArrayList<>();

        void setItems(List<Object> newItems) {
            items.clear();
            items.addAll(newItems);
            notifyDataSetChanged();
        }

        @Override
        public int getItemViewType(int position) {
            Object item = items.get(position);
            if (item instanceof Lesson) return TYPE_LESSON;
            if (item instanceof Category) return TYPE_CATEGORY;
            return TYPE_AD;
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
            } else if (viewType == TYPE_CATEGORY) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.android_studio_category_item, parent, false);
                return new CategoryHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.android_studio_lesson_item, parent, false);
                return new LessonHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int type = getItemViewType(position);
            if (type == TYPE_AD) {
                AdHolder adHolder = (AdHolder) holder;
                adHolder.adView.loadAd(new AdRequest.Builder().build(), new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        adHolder.adView.setVisibility(View.GONE);
                    }
                });
            } else if (type == TYPE_CATEGORY) {
                Category category = (Category) items.get(position);
                ((CategoryHolder) holder).bind(category);
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

        static class CategoryHolder extends RecyclerView.ViewHolder {
            final ImageView icon;
            final TextView title;

            CategoryHolder(@NonNull View itemView) {
                super(itemView);
                icon = itemView.findViewById(R.id.category_icon);
                title = itemView.findViewById(R.id.category_title);
            }

            void bind(Category category) {
                if (category.iconRes != 0) {
                    icon.setImageResource(category.iconRes);
                    icon.setVisibility(View.VISIBLE);
                } else {
                    icon.setVisibility(View.GONE);
                }
                title.setText(category.title);
            }
        }
    }
}
