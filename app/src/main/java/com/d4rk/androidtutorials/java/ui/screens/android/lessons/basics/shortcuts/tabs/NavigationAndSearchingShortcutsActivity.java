package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.ads.AdUtils;
import com.d4rk.androidtutorials.java.databinding.ActivityShortcutsNavigationAndSearchingBinding;
import com.d4rk.androidtutorials.java.databinding.ItemShortcutBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;

import java.util.List;

import me.zhanghai.android.fastscroll.FastScrollerBuilder;

public class NavigationAndSearchingShortcutsActivity extends UpNavigationActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityShortcutsNavigationAndSearchingBinding binding = ActivityShortcutsNavigationAndSearchingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeDelegate.apply(this, binding.shortcutList);

        AdUtils.loadBanner(binding.adView);
        new FastScrollerBuilder(binding.shortcutList).useMd2Style().build();

        List<Shortcut> shortcuts = List.of(
                new Shortcut(getString(R.string.press_shift_twice), getString(R.string.search_everything)),
                new Shortcut("Ctrl + F", getString(R.string.find)),
                new Shortcut("F3", getString(R.string.find_next)),
                new Shortcut("Shift + F3", getString(R.string.find_previous)),
                new Shortcut("Ctrl + R", getString(R.string.replace)),
                new Shortcut("Ctrl + Shift + A", getString(R.string.find_action)),
                new Shortcut("Ctrl + Alt + Shift + N", getString(R.string.search_by_symbol_name)),
                new Shortcut("Ctrl + N", getString(R.string.find_class)),
                new Shortcut("Ctrl + Shift + N", getString(R.string.find_file)),
                new Shortcut("Ctrl + Shift + F", getString(R.string.find_path)),
                new Shortcut("Ctrl + F12", getString(R.string.open_file_structure)),
                new Shortcut("Alt + Right/Left Arrow", getString(R.string.navigate_between_open_tabs)),
                new Shortcut("F4/Ctrl +Enter", getString(R.string.jump_to_source)),
                new Shortcut("Shift + F4", getString(R.string.open_current_editor_tab_in_new_window)),
                new Shortcut("Ctrl + E", getString(R.string.recently_opened_files)),
                new Shortcut("Ctrl + Shift + E", getString(R.string.recently_edited_files)),
                new Shortcut("Ctrl + Shift + Backspace", getString(R.string.go_to_last_edit_location)),
                new Shortcut("Ctrl + F4", getString(R.string.close_active_editor_tabs)),
                new Shortcut("Esc", getString(R.string.return_to_editor_window)),
                new Shortcut("Shift + Esc", getString(R.string.hide_active_window)),
                new Shortcut("Ctrl + G", getString(R.string.go_to_line)),
                new Shortcut("Ctrl + H", getString(R.string.open_type_hierarchy)),
                new Shortcut("Ctrl + Shift + H", getString(R.string.open_v_hierarchy)),
                new Shortcut("Ctrl + Alt + H", getString(R.string.open_call_hierarchy))
        );

        binding.shortcutList.setLayoutManager(new LinearLayoutManager(this));
        binding.shortcutList.setAdapter(new ShortcutsAdapter(shortcuts));
    }

    private static class ShortcutsAdapter extends RecyclerView.Adapter<ShortcutsAdapter.ShortcutHolder> {
        private final List<Shortcut> items;

        ShortcutsAdapter(List<Shortcut> items) {
            this.items = items;
        }

        @NonNull
        @Override
        public ShortcutHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemShortcutBinding binding = ItemShortcutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ShortcutHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull ShortcutHolder holder, int position) {
            Shortcut item = items.get(position);
            holder.bind(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class ShortcutHolder extends RecyclerView.ViewHolder {
            private final ItemShortcutBinding binding;

            ShortcutHolder(@NonNull ItemShortcutBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(@NonNull Shortcut shortcut) {
                binding.shortcutKey.setText(shortcut.key());
                binding.shortcutDescription.setText(shortcut.description());
            }
        }
    }

    private record Shortcut(String key, String description) { }
}