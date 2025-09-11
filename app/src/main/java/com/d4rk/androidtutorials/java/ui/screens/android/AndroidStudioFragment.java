package com.d4rk.androidtutorials.java.ui.screens.android;

import android.content.Context;
import android.content.Intent;
import android.content.res.XmlResourceParser;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
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
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.views.NativeAdBannerView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.d4rk.androidtutorials.java.utils.ConsentUtils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class AndroidStudioFragment extends Fragment {

    private static boolean mobileAdsInitialized = false;
    private final List<Object> allItems = new ArrayList<>();
    private LessonsAdapter adapter;
    private boolean showAds;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_android_studio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showAds = ConsentUtils.canShowAds(requireContext());
        if (showAds) {
            ensureMobileAdsInitialized();
        }
        RecyclerView list = view.findViewById(R.id.lessons_list);
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LessonsAdapter();
        list.setAdapter(adapter);
        if (showAds) {
            list.addItemDecoration(new LessonAdSpacingDecoration(requireContext()));
        }
        allItems.clear();
        allItems.addAll(loadItems());
        populateAdapter(allItems, showAds);

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_android_studio, menu);
                MenuItem searchItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) searchItem.getActionView();
                if (searchView != null) {
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
                }
                searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                        filterLessons("");
                        return true;
                    }
                });
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void ensureMobileAdsInitialized() {
        if (!mobileAdsInitialized) {
            MobileAds.initialize(requireContext());
            mobileAdsInitialized = true;
        }
    }

    private List<Object> loadItems() {
        List<Object> items = new ArrayList<>();
        try (XmlResourceParser parser = getResources().getXml(R.xml.preferences_android_studio)) {
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
        }
        return items;
    }

    private void populateAdapter(List<Object> source, boolean showAds) {
        List<Object> items = new ArrayList<>();
        List<Integer> eligible = new ArrayList<>();
        int lessonCount = 0;
        boolean firstInCategory = true;
        for (Object item : source) {
            if (item instanceof Category) {
                firstInCategory = true;
            } else if (item instanceof Lesson) {
                if (!firstInCategory) {
                    eligible.add(lessonCount);
                }
                lessonCount++;
                firstInCategory = false;
            }
        }
        int adCount = showAds ? lessonCount / 3 : 0;
        Collections.shuffle(eligible, new Random());
        if (adCount > eligible.size()) {
            adCount = eligible.size();
        }
        Set<Integer> adPositions = new HashSet<>(eligible.subList(0, adCount));
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

    private void filterLessons(String query) {
        String lower = query == null ? "" : query.toLowerCase();
        if (lower.isEmpty()) {
        populateAdapter(allItems, showAds);
            return;
        }
        List<Object> filtered = new ArrayList<>();
        Category lastCategory = null;
        boolean categoryAdded = false;
        for (Object item : allItems) {
            if (item instanceof Category) {
                lastCategory = (Category) item;
                categoryAdded = false;
            } else if (item instanceof Lesson l) {
                if (l.title != null && l.title.toLowerCase().contains(lower)) {
                    if (lastCategory != null && !categoryAdded) {
                        filtered.add(lastCategory);
                        categoryAdded = true;
                    }
                    filtered.add(l);
                }
            }
        }
        populateAdapter(filtered, showAds);
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

    private static class LessonAdSpacingDecoration extends RecyclerView.ItemDecoration {
        private final int spacing;

        LessonAdSpacingDecoration(@NonNull Context context) {
            spacing = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                    context.getResources().getDisplayMetrics());
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                                   @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            LessonsAdapter adapter = (LessonsAdapter) parent.getAdapter();
            if (adapter == null) return;
            int position = parent.getChildAdapterPosition(view);
            if (position == RecyclerView.NO_POSITION) return;
            int type = adapter.getItemViewType(position);
            if (type == LessonsAdapter.TYPE_LESSON || type == LessonsAdapter.TYPE_AD) {
                outRect.bottom = spacing;
            }
        }
    }

    private static class LessonsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        static final int TYPE_LESSON = 0;
        static final int TYPE_AD = 1;
        static final int TYPE_CATEGORY = 2;
        private final List<Object> items = new ArrayList<>();

        void setItems(List<Object> newItems) {
            DiffUtil.DiffResult diff = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return items.size();
                }

                @Override
                public int getNewListSize() {
                    return newItems.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    Object oldItem = items.get(oldItemPosition);
                    Object newItem = newItems.get(newItemPosition);
                    if (oldItem instanceof Lesson oldLesson && newItem instanceof Lesson newLesson) {
                        return Objects.equals(oldLesson.title, newLesson.title);
                    }
                    if (oldItem instanceof Category oldCat && newItem instanceof Category newCat) {
                        return Objects.equals(oldCat.title, newCat.title);
                    }
                    return oldItem instanceof AdItem && newItem instanceof AdItem;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Object oldItem = items.get(oldItemPosition);
                    Object newItem = newItems.get(newItemPosition);
                    if (oldItem instanceof Lesson oldLesson && newItem instanceof Lesson newLesson) {
                        return Objects.equals(oldLesson.title, newLesson.title)
                                && Objects.equals(oldLesson.summary, newLesson.summary)
                                && oldLesson.iconRes == newLesson.iconRes;
                    }
                    if (oldItem instanceof Category oldCat && newItem instanceof Category newCat) {
                        return Objects.equals(oldCat.title, newCat.title)
                                && oldCat.iconRes == newCat.iconRes;
                    }
                    return true;
                }
            });
            items.clear();
            items.addAll(newItems);
            diff.dispatchUpdatesTo(this);
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
                adView.setNativeAdLayout(R.layout.ad_android_studio_list);
                return new AdHolder(adView);
            } else if (viewType == TYPE_CATEGORY) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_android_studio_category, parent, false);
                return new CategoryHolder(view);
            } else {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_android_studio_lesson, parent, false);
                return new LessonHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            int type = getItemViewType(pos);
            if (type == TYPE_AD) {
                AdHolder adHolder = (AdHolder) holder;
                adHolder.adView.loadAd(new AdRequest.Builder().build(), new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        adHolder.adView.setVisibility(View.GONE);
                    }
                });
            } else if (type == TYPE_CATEGORY) {
                Category category = (Category) items.get(pos);
                ((CategoryHolder) holder).bind(category);
            } else {
                Lesson lesson = (Lesson) items.get(pos);
                boolean first = pos > 0 && getItemViewType(pos - 1) == TYPE_CATEGORY;
                boolean last = pos == getItemCount() - 1 || getItemViewType(pos + 1) == TYPE_CATEGORY;
                ((LessonHolder) holder).bind(lesson, first, last);
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
            final MaterialCardView card;
            final ImageView icon;
            final TextView title;
            final TextView summary;

            LessonHolder(@NonNull View itemView) {
                super(itemView);
                card = (MaterialCardView) itemView;
                icon = itemView.findViewById(R.id.lesson_icon);
                title = itemView.findViewById(R.id.lesson_title);
                summary = itemView.findViewById(R.id.lesson_summary);
            }

            void bind(Lesson lesson, boolean first, boolean last) {
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
                applyCorners(first, last);
            }

            private void applyCorners(boolean first, boolean last) {
                float dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                        itemView.getResources().getDisplayMetrics());
                float dp24 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        itemView.getResources().getDisplayMetrics());
                ShapeAppearanceModel.Builder builder = card.getShapeAppearanceModel().toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, first ? dp24 : dp4)
                        .setTopRightCorner(CornerFamily.ROUNDED, first ? dp24 : dp4)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, last ? dp24 : dp4)
                        .setBottomRightCorner(CornerFamily.ROUNDED, last ? dp24 : dp4);
                card.setShapeAppearanceModel(builder.build());
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
