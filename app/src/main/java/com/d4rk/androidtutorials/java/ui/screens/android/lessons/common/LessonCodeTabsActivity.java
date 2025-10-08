package com.d4rk.androidtutorials.java.ui.screens.android.lessons.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.d4rk.androidtutorials.java.databinding.ActivityTabLayoutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Shared activity for displaying lesson code/layout tab combinations.
 */
public class LessonCodeTabsActivity extends UpNavigationActivity {
    public static final String EXTRA_PAGES =
            "com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.EXTRA_PAGES";
    public static final String EXTRA_TITLE_RES =
            "com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.EXTRA_TITLE_RES";
    public static final String EXTRA_TITLE =
            "com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.EXTRA_TITLE";

    @Nullable
    private ActivityTabLayoutBinding binding;

    /**
     * Creates an {@link Intent} for launching the shared code tabs activity with the given title
     * resource and page specifications.
     */
    @NonNull
    public static Intent createIntent(@NonNull Context context, int titleResId,
                                      @NonNull List<PageSpec> pages) {
        Intent intent = new Intent(context, LessonCodeTabsActivity.class);
        intent.putExtra(EXTRA_TITLE_RES, titleResId);
        intent.putParcelableArrayListExtra(EXTRA_PAGES, new ArrayList<>(pages));
        return intent;
    }

    /**
     * Creates an {@link Intent} for launching the shared code tabs activity with a custom title
     * string.
     */
    @NonNull
    public static Intent createIntent(@NonNull Context context, @NonNull CharSequence title,
                                      @NonNull List<PageSpec> pages) {
        Intent intent = new Intent(context, LessonCodeTabsActivity.class);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putParcelableArrayListExtra(EXTRA_PAGES, new ArrayList<>(pages));
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTabLayoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        applyTitleFromIntent();

        List<PageSpec> pages = resolvePages();
        LessonCodeTabsAdapter adapter = new LessonCodeTabsAdapter(this, pages);
        binding.viewpager.setAdapter(adapter);
        new TabLayoutMediator(binding.tabs, binding.viewpager,
                (tab, position) -> tab.setText(adapter.getPageTitle(position))).attach();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void applyTitleFromIntent() {
        Intent intent = getIntent();
        int titleRes = intent.getIntExtra(EXTRA_TITLE_RES, 0);
        if (titleRes != 0) {
            setTitle(titleRes);
        } else {
            CharSequence title = intent.getCharSequenceExtra(EXTRA_TITLE);
            if (title != null) {
                setTitle(title);
            }
        }
    }

    @NonNull
    private List<PageSpec> resolvePages() {
        List<PageSpec> pages = getPagesFromIntent();
        if (pages == null || pages.isEmpty()) {
            pages = buildPages();
        }
        if (pages == null || pages.isEmpty()) {
            throw new IllegalStateException("No pages supplied to LessonCodeTabsActivity");
        }
        return Collections.unmodifiableList(new ArrayList<>(pages));
    }

    @Nullable
    private List<PageSpec> getPagesFromIntent() {
        ArrayList<PageSpec> specs = getIntent().getParcelableArrayListExtra(EXTRA_PAGES);
        if (specs == null || specs.isEmpty()) {
            return null;
        }
        return specs;
    }

    /**
     * Subclasses can override to provide tab specifications without relying on intent extras.
     */
    @NonNull
    protected List<PageSpec> buildPages() {
        return Collections.emptyList();
    }

    private static class LessonCodeTabsAdapter extends FragmentStateAdapter {
        private final FragmentActivity host;
        private final List<PageSpec> pages;

        LessonCodeTabsAdapter(@NonNull FragmentActivity activity, @NonNull List<PageSpec> pages) {
            super(activity);
            this.host = activity;
            this.pages = pages;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return pages.get(position).createFragment(host);
        }

        @Override
        public int getItemCount() {
            return pages.size();
        }

        CharSequence getPageTitle(int position) {
            return pages.get(position).getTitle();
        }
    }

    public static class PageSpec implements Parcelable {
        private final String fragmentClassName;
        @Nullable
        private final Bundle arguments;
        private final String title;

        private PageSpec(@NonNull String fragmentClassName, @Nullable Bundle arguments,
                         @NonNull String title) {
            this.fragmentClassName = fragmentClassName;
            this.arguments = arguments == null ? null : new Bundle(arguments);
            this.title = title;
        }

        protected PageSpec(@NonNull Parcel in) {
            fragmentClassName = Objects.requireNonNull(in.readString());
            arguments = in.readBundle(PageSpec.class.getClassLoader());
            title = Objects.requireNonNull(in.readString());
        }

        @NonNull
        public static PageSpec of(@NonNull Class<? extends Fragment> fragmentClass,
                                  @NonNull String title) {
            return new PageSpec(fragmentClass.getName(), null, title);
        }

        @NonNull
        public static PageSpec of(@NonNull Class<? extends Fragment> fragmentClass,
                                  @Nullable Bundle arguments, @NonNull String title) {
            return new PageSpec(fragmentClass.getName(), arguments, title);
        }

        @NonNull
        Fragment createFragment(@NonNull Context context) {
            Bundle args = arguments == null ? null : new Bundle(arguments);
            return Fragment.instantiate(context, fragmentClassName, args);
        }

        @NonNull
        CharSequence getTitle() {
            return title;
        }

        @Override
        public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeString(fragmentClassName);
            dest.writeBundle(arguments);
            dest.writeString(title);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PageSpec> CREATOR = new Creator<PageSpec>() {
            @Override
            public PageSpec createFromParcel(Parcel in) {
                return new PageSpec(in);
            }

            @Override
            public PageSpec[] newArray(int size) {
                return new PageSpec[size];
            }
        };
    }
}
