package com.d4rk.androidtutorials.java.ui.screens.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.d4rk.androidtutorials.java.data.model.PromotedApp;
import com.d4rk.androidtutorials.java.databinding.ItemPromotedAppBinding;

import java.util.Objects;

public class PromotedAppsAdapter extends ListAdapter<PromotedApp, PromotedAppsAdapter.PromotedAppViewHolder> {

    public interface PromotedAppActionListener {
        void onOpenApp(@NonNull PromotedApp app);

        void onShareApp(@NonNull PromotedApp app);
    }

    private final PromotedAppActionListener actionListener;

    public PromotedAppsAdapter(@NonNull PromotedAppActionListener actionListener) {
        super(DIFF_CALLBACK);
        this.actionListener = actionListener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public PromotedAppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPromotedAppBinding binding = ItemPromotedAppBinding.inflate(inflater, parent, false);
        return new PromotedAppViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotedAppViewHolder holder, int position) {
        holder.bind(getItem(position), actionListener);
    }

    @Override
    public long getItemId(int position) {
        return Objects.hashCode(getItem(position).packageName());
    }

    static class PromotedAppViewHolder extends RecyclerView.ViewHolder {

        private final ItemPromotedAppBinding binding;

        PromotedAppViewHolder(@NonNull ItemPromotedAppBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull PromotedApp app, @NonNull PromotedAppActionListener actionListener) {
            Glide.with(binding.appIcon.getContext())
                    .load(app.iconUrl())
                    .centerInside()
                    .into(binding.appIcon);
            binding.appName.setText(app.name());
            binding.appDescription.setVisibility(View.GONE);
            binding.appButton.setOnClickListener(v -> actionListener.onOpenApp(app));
            binding.shareButton.setOnClickListener(v -> actionListener.onShareApp(app));
        }
    }

    private static final DiffUtil.ItemCallback<PromotedApp> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<>() {
                @Override
                public boolean areItemsTheSame(@NonNull PromotedApp oldItem, @NonNull PromotedApp newItem) {
                    return Objects.equals(oldItem.packageName(), newItem.packageName());
                }

                @Override
                public boolean areContentsTheSame(@NonNull PromotedApp oldItem, @NonNull PromotedApp newItem) {
                    return oldItem.equals(newItem);
                }
            };
}
