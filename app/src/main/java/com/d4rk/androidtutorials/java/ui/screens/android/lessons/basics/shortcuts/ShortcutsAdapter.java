package com.d4rk.androidtutorials.java.ui.screens.android.lessons.basics.shortcuts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;

public final class ShortcutsAdapter
        extends ListAdapter<ShortcutItem, ShortcutsAdapter.ShortcutViewHolder> {

    // DiffUtil: only update what actually changed.
    private static final DiffUtil.ItemCallback<ShortcutItem> DIFF = new DiffUtil.ItemCallback<>() {
        @Override
        public boolean areItemsTheSame(@NonNull ShortcutItem oldItem, @NonNull ShortcutItem newItem) {
            // Keys are unique in your dataset (e.g., "Ctrl + S")
            return oldItem.key().equals(newItem.key());
        }

        @Override
        public boolean areContentsTheSame(@NonNull ShortcutItem oldItem, @NonNull ShortcutItem newItem) {
            // If description resource id is the same, content is the same
            return oldItem.descriptionResId() == newItem.descriptionResId();
        }
    };

    public ShortcutsAdapter() {
        super(DIFF);
        setHasStableIds(false);
    }

    @NonNull
    @Override
    public ShortcutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_shortcut, parent, false);
        return new ShortcutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortcutViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    // You can keep calling adapter.submitList(list) from outside; ListAdapter handles diff+dispatch.

    // Make visibility at least as wide as the adapter’s public API.
    public static final class ShortcutViewHolder extends RecyclerView.ViewHolder {

        private final TextView keyTextView;
        private final TextView descriptionTextView;

        public ShortcutViewHolder(@NonNull View itemView) {
            super(itemView);
            keyTextView = itemView.findViewById(R.id.shortcut_key);
            descriptionTextView = itemView.findViewById(R.id.shortcut_description);
        }

        void bind(ShortcutItem item) {
            keyTextView.setText(item.key());
            descriptionTextView.setText(item.descriptionResId());
        }
    }
}