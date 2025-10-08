package com.d4rk.androidtutorials.java.ui.screens.android.lessons.data.room;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRoomBinding;
import com.d4rk.androidtutorials.java.databinding.ItemNoteBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.common.LessonCodeTabsActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.data.room.tabs.RoomTabCodeFragment;
import com.d4rk.androidtutorials.java.ui.screens.android.lessons.data.room.tabs.RoomTabLayoutFragment;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Demonstrates basic Room usage by inserting and reading notes.
 */
public class RoomActivity extends UpNavigationActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private ActivityRoomBinding binding;
    private NotesAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());

        db = AppDatabase.getInstance(this);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter();
        binding.recyclerView.setAdapter(adapter);

        loadNotes();

        binding.buttonAdd.setOnClickListener(v -> {
            String text = String.valueOf(binding.editTextNote.getText()).trim();
            if (!text.isEmpty()) {
                if (!executor.isShutdown()) {
                    executor.execute(() -> {
                        db.noteDao().insert(new Note(text));
                        runOnUiThread(() -> {
                            binding.editTextNote.setText("");
                            loadNotes();
                        });
                    });
                }
            }
        });

        binding.floatingButtonShowSyntax.setOnClickListener(v -> startActivity(
                LessonCodeTabsActivity.createIntent(
                        this,
                        R.string.room_database,
                        Arrays.asList(
                                LessonCodeTabsActivity.PageSpec.of(
                                        RoomTabCodeFragment.class,
                                        getString(R.string.code_java)
                                ),
                                LessonCodeTabsActivity.PageSpec.of(
                                        RoomTabLayoutFragment.class,
                                        getString(R.string.layout_xml)
                                )
                        )
                )));
        handler.postDelayed(() -> binding.floatingButtonShowSyntax.shrink(), 5000);
    }

    private void loadNotes() {
        if (!executor.isShutdown()) {
            executor.execute(() -> {
                List<Note> notes = db.noteDao().getAll();
                runOnUiThread(() -> adapter.submitList(notes));
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdown();
        handler.removeCallbacksAndMessages(null);
    }

    private static class NotesAdapter extends ListAdapter<Note, NotesAdapter.NoteViewHolder> {
        private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK =
                new DiffUtil.ItemCallback<>() {
                    @Override
                    public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                        return oldItem.id == newItem.id;
                    }

                    @Override
                    public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
                        return oldItem.text.equals(newItem.text);
                    }
                };

        NotesAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ItemNoteBinding binding = ItemNoteBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return new NoteViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            holder.bind(getItem(position));
        }

        static class NoteViewHolder extends RecyclerView.ViewHolder {
            private final ItemNoteBinding binding;

            NoteViewHolder(@NonNull ItemNoteBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(@NonNull Note note) {
                binding.textViewNote.setText(note.getText());
            }
        }
    }

}
