package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.ShortcutItem;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.ShortcutsAdapter;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.ShortcutsRepository;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;
import com.google.android.gms.ads.AdView;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import me.zhanghai.android.fastscroll.FastScrollScrollView;
import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class ShortcutsCategoryActivity extends UpNavigationActivity {

    public static final String EXTRA_LAYOUT_RES_ID =
            "com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs.EXTRA_LAYOUT_RES_ID";
    public static final String EXTRA_TITLE_RES_ID =
            "com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs.EXTRA_TITLE_RES_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        int layoutResId = intent.getIntExtra(EXTRA_LAYOUT_RES_ID, 0);
        int titleResId = intent.getIntExtra(EXTRA_TITLE_RES_ID, R.string.shortcuts);

        if (layoutResId == 0) {
            finish();
            return;
        }

        setContentView(layoutResId);
        setTitle(titleResId);

        RecyclerView recyclerView = setupShortcuts(layoutResId);

        View contentView = findViewById(android.R.id.content);
        if (contentView instanceof ViewGroup) {
            View root = ((ViewGroup) contentView).getChildAt(0);
            if (root != null) {
                EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), root);
            }
        }

        AdView adView = findViewById(R.id.ad_view);
        if (adView != null) {
            AdUtils.loadBanner(adView);
        }

        if (recyclerView != null) {
            new FastScrollerBuilder(recyclerView).useMd2Style().build();
        } else {
            FastScrollScrollView scrollView = findViewById(R.id.scroll_view);
            if (scrollView != null) {
                new FastScrollerBuilder(scrollView).useMd2Style().build();
            }
        }
    }

    private RecyclerView setupShortcuts(int layoutResId) {
        RecyclerView recyclerView = findViewById(R.id.shortcut_list);
        if (recyclerView == null) {
            return null;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ShortcutsAdapter adapter = new ShortcutsAdapter();
        recyclerView.setAdapter(adapter);

        List<ShortcutItem> shortcuts = ShortcutsRepository.getShortcuts(layoutResId);
        adapter.submitList(shortcuts);
        return recyclerView;
    }
}
