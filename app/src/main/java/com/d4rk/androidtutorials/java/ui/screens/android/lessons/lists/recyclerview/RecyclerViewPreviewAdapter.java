package com.d4rk.androidtutorials.java.ui.screens.android.lessons.lists.recyclerview;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.databinding.ItemRecyclerViewLessonBinding;

import java.util.List;

public class RecyclerViewPreviewAdapter extends RecyclerView.Adapter<RecyclerViewPreviewAdapter.LessonViewHolder> {

    private final List<String> lessons;

    public RecyclerViewPreviewAdapter(@NonNull List<String> lessons) {
        this.lessons = lessons;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecyclerViewLessonBinding binding = ItemRecyclerViewLessonBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new LessonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        holder.bind(lessons.get(position));
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    static class LessonViewHolder extends RecyclerView.ViewHolder {
        private final ItemRecyclerViewLessonBinding binding;

        LessonViewHolder(ItemRecyclerViewLessonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(@NonNull String lessonName) {
            binding.lessonTitle.setText(lessonName);
        }
    }
}
