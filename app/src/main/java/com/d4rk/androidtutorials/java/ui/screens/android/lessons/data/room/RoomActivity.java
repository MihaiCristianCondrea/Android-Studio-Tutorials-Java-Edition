package com.d4rk.androidtutorials.java.ui.screens.android.lessons.data.room;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.d4rk.androidtutorials.java.R;
import com.d4rk.androidtutorials.java.databinding.ActivityRoomBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.UpNavigationActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeDelegate;
import com.google.android.material.textview.MaterialTextView;

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

        EdgeToEdgeDelegate edgeToEdgeDelegate = new EdgeToEdgeDelegate(this);
        edgeToEdgeDelegate.applyEdgeToEdge(binding.constraintLayout);

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

        binding.floatingButtonShowSyntax.setOnClickListener(v ->
                startActivity(new Intent(RoomActivity.this, RoomCodeActivity.class)));
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
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_note, parent, false);
            return new NoteViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
            holder.textView.setText(getItem(position).getText());
        }

        static class NoteViewHolder extends RecyclerView.ViewHolder {
            final MaterialTextView textView;

            NoteViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textViewNote);
            }
        }
    }

}
