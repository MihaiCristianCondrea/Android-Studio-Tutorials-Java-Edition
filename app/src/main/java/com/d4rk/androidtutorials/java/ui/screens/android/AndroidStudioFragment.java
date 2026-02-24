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
import android.widget.FrameLayout;

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
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.ads.views.NativeAdBannerView;
import com.d4rk.androidtutorials.java.databinding.FragmentAndroidStudioBinding;
import com.d4rk.androidtutorials.java.databinding.ItemPreferenceBinding;
import com.d4rk.androidtutorials.java.databinding.ItemPreferenceCategoryBinding;
import com.d4rk.androidtutorials.java.databinding.ItemPreferenceWidgetOpenInNewBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AndroidStudioFragment extends Fragment {

    private static boolean mobileAdsInitialized = false;
    private final List<Object> allItems = new ArrayList<>();
    private final Set<Integer> stableAdLessonIndexes = new LinkedHashSet<>();
    private final Map<Lesson, Integer> lessonIndexLookup = new IdentityHashMap<>();
    private LessonsAdapter adapter;
    private FragmentAndroidStudioBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAndroidStudioBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ensureMobileAdsInitialized();
        RecyclerView list = binding.lessonsList;
        list.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new LessonsAdapter();
        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        list.addItemDecoration(new LessonAdSpacingDecoration(requireContext()));
        allItems.clear();
        allItems.addAll(loadItems());
        cacheStableAdPositions(allItems);
        populateAdapter(allItems);

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void ensureMobileAdsInitialized() {
        if (!mobileAdsInitialized) {
            AdUtils.initialize(requireContext());
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
                        currentLesson.opensInBrowser = isBrowserIntent(intent);
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

    private boolean isBrowserIntent(Intent intent) {
        if (intent.getComponent() != null) {
            return false;
        }
        Uri data = intent.getData();
        if (data == null) {
            return false;
        }
        String scheme = data.getScheme();
        if (scheme == null) {
            return false;
        }
        return Intent.ACTION_VIEW.equals(intent.getAction())
                && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
    }

    private void populateAdapter(List<Object> source) {
        List<Object> items = new ArrayList<>();
        for (Object item : source) {
            if (item instanceof Lesson) {
                Integer lessonIndex = lessonIndexLookup.get(item);
                if (lessonIndex != null && stableAdLessonIndexes.contains(lessonIndex)) {
                    items.add(new AdItem(lessonIndex));
                }
                items.add(item);
            } else {
                items.add(item);
            }
        }
        adapter.setItems(items);
    }

    private void cacheStableAdPositions(List<Object> source) {
        lessonIndexLookup.clear();
        stableAdLessonIndexes.clear();
        List<Integer> eligibleIndexes = new ArrayList<>();
        int lessonCount = 0;
        boolean firstInCategory = true;
        for (Object item : source) {
            if (item instanceof Category) {
                firstInCategory = true;
            } else if (item instanceof Lesson lesson) {
                lessonIndexLookup.put(lesson, lessonCount);
                if (!firstInCategory) {
                    eligibleIndexes.add(lessonCount);
                }
                lessonCount++;
                firstInCategory = false;
            }
        }

        int adCount = Math.min(lessonCount / 3, eligibleIndexes.size());
        if (adCount == 0) {
            return;
        }

        for (int slot = 1; slot <= adCount; slot++) {
            int eligiblePosition = (int) Math.floor((slot * (double) eligibleIndexes.size()) / (adCount + 1));
            stableAdLessonIndexes.add(eligibleIndexes.get(eligiblePosition));
        }
    }

    private void filterLessons(String query) {
        String lower = query == null ? "" : query.toLowerCase(Locale.ROOT);
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
            } else if (item instanceof Lesson l) {
                if (l.title != null && l.title.toLowerCase(Locale.ROOT).contains(lower)) {
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

    private static class AdItem {
        private final long stableId;

        AdItem(int lessonIndex) {
            stableId = lessonIndex;
        }
    }

    private static class Lesson {
        String title;
        String summary;
        int iconRes;
        Intent intent;
        boolean opensInBrowser;
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

        LessonsAdapter() {
            setHasStableIds(true);
        }

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
                    if (oldItem instanceof AdItem oldAd && newItem instanceof AdItem newAd) {
                        return oldAd.stableId == newAd.stableId;
                    }
                    return false;
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Object oldItem = items.get(oldItemPosition);
                    Object newItem = newItems.get(newItemPosition);
                    if (oldItem instanceof Lesson oldLesson && newItem instanceof Lesson newLesson) {
                        return Objects.equals(oldLesson.title, newLesson.title)
                                && Objects.equals(oldLesson.summary, newLesson.summary)
                                && oldLesson.iconRes == newLesson.iconRes
                                && oldLesson.opensInBrowser == newLesson.opensInBrowser;
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

        @Override
        public long getItemId(int position) {
            Object item = items.get(position);
            if (item instanceof Lesson lesson) {
                return Objects.hash(TYPE_LESSON, lesson.title, lesson.summary, lesson.iconRes);
            }
            if (item instanceof Category category) {
                return Objects.hash(TYPE_CATEGORY, category.title, category.iconRes);
            }
            AdItem adItem = (AdItem) item;
            return Objects.hash(TYPE_AD, adItem.stableId);
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
                adView.setNativeAdUnitId(R.string.native_ad_lessons_list_unit_id);
                return new AdHolder(adView);
            } else if (viewType == TYPE_CATEGORY) {
                ItemPreferenceCategoryBinding binding = ItemPreferenceCategoryBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
                return new CategoryHolder(binding);
            } else {
                ItemPreferenceBinding binding = ItemPreferenceBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false
                );
                return new LessonHolder(binding);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            int type = getItemViewType(pos);
            if (type == TYPE_AD) {
                AdHolder adHolder = (AdHolder) holder;
                adHolder.bind();
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
            private boolean adRequested;

            AdHolder(@NonNull NativeAdBannerView itemView) {
                super(itemView);
                this.adView = itemView;
            }

            void bind() {
                if (adRequested) {
                    return;
                }
                adRequested = true;
                adView.loadAd(new AdListener() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError error) {
                        adView.setVisibility(View.GONE);
                    }
                });
            }
        }

        static class LessonHolder extends RecyclerView.ViewHolder {
            private final ItemPreferenceBinding binding;
            private final ItemPreferenceWidgetOpenInNewBinding widgetBinding;
            private final ShapeAppearanceModel roundedAllCorners;
            private final ShapeAppearanceModel roundedTopOnly;
            private final ShapeAppearanceModel roundedBottomOnly;
            private final ShapeAppearanceModel compactCorners;

            LessonHolder(@NonNull ItemPreferenceBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                widgetBinding = ItemPreferenceWidgetOpenInNewBinding.inflate(
                        LayoutInflater.from(binding.getRoot().getContext()),
                        binding.widgetFrame,
                        true
                );

                float dp4 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                        itemView.getResources().getDisplayMetrics());
                float dp24 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24,
                        itemView.getResources().getDisplayMetrics());
                ShapeAppearanceModel base = binding.lessonCard.getShapeAppearanceModel();
                roundedAllCorners = base.toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, dp24)
                        .setTopRightCorner(CornerFamily.ROUNDED, dp24)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, dp24)
                        .setBottomRightCorner(CornerFamily.ROUNDED, dp24)
                        .build();
                roundedTopOnly = base.toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, dp24)
                        .setTopRightCorner(CornerFamily.ROUNDED, dp24)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, dp4)
                        .setBottomRightCorner(CornerFamily.ROUNDED, dp4)
                        .build();
                roundedBottomOnly = base.toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, dp4)
                        .setTopRightCorner(CornerFamily.ROUNDED, dp4)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, dp24)
                        .setBottomRightCorner(CornerFamily.ROUNDED, dp24)
                        .build();
                compactCorners = base.toBuilder()
                        .setTopLeftCorner(CornerFamily.ROUNDED, dp4)
                        .setTopRightCorner(CornerFamily.ROUNDED, dp4)
                        .setBottomLeftCorner(CornerFamily.ROUNDED, dp4)
                        .setBottomRightCorner(CornerFamily.ROUNDED, dp4)
                        .build();
            }

            void bind(Lesson lesson, boolean first, boolean last) {
                if (lesson.iconRes != 0) {
                    binding.icon.setImageResource(lesson.iconRes);
                    binding.icon.setVisibility(View.VISIBLE);
                } else {
                    binding.icon.setVisibility(View.GONE);
                }
                binding.title.setText(lesson.title);
                if (lesson.summary != null) {
                    binding.summary.setText(lesson.summary);
                    binding.summary.setVisibility(View.VISIBLE);
                } else {
                    binding.summary.setVisibility(View.GONE);
                }
                boolean showExternalButton = lesson.opensInBrowser && lesson.intent != null;
                FrameLayout widgetFrame = binding.widgetFrame;
                MaterialButton externalButton = widgetBinding.openInNew;
                widgetFrame.setVisibility(showExternalButton ? View.VISIBLE : View.GONE);
                externalButton.setVisibility(showExternalButton ? View.VISIBLE : View.GONE);
                externalButton.setEnabled(showExternalButton);
                externalButton.setFocusableInTouchMode(showExternalButton);
                if (showExternalButton) {
                    externalButton.setClickable(true);
                    externalButton.setFocusable(true);
                    externalButton.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_YES);
                    externalButton.setOnClickListener(v -> v.getContext().startActivity(lesson.intent));
                } else {
                    externalButton.setClickable(false);
                    externalButton.setFocusable(false);
                    externalButton.setImportantForAccessibility(View.IMPORTANT_FOR_ACCESSIBILITY_NO);
                    externalButton.setOnClickListener(null);
                }
                itemView.setOnClickListener(v -> {
                    if (lesson.intent != null) {
                        v.getContext().startActivity(lesson.intent);
                    }
                });
                applyCorners(first, last);
            }

            private void applyCorners(boolean first, boolean last) {
                if (first && last) {
                    binding.lessonCard.setShapeAppearanceModel(roundedAllCorners);
                } else if (first) {
                    binding.lessonCard.setShapeAppearanceModel(roundedTopOnly);
                } else if (last) {
                    binding.lessonCard.setShapeAppearanceModel(roundedBottomOnly);
                } else {
                    binding.lessonCard.setShapeAppearanceModel(compactCorners);
                }
            }
        }

        static class CategoryHolder extends RecyclerView.ViewHolder {
            private final ItemPreferenceCategoryBinding binding;

            CategoryHolder(@NonNull ItemPreferenceCategoryBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(Category category) {
                if (category.iconRes != 0) {
                    binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(category.iconRes, 0, 0, 0);
                } else {
                    binding.title.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                }
                binding.title.setText(category.title);
            }
        }
    }
}
