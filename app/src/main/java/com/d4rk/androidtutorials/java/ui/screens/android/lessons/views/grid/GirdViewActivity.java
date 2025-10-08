package com.d4rk.androidtutorials.java.ui.screens.android.lessons.views.grid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.d4rk.androidtutorials.java.databinding.ActivityGridViewBinding;
import com.d4rk.androidtutorials.java.databinding.ItemGridLetterBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

public class GirdViewActivity extends SyntaxFabActivity {

    private final String[] numbers = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private ActivityGridViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGridViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        ArrayAdapter<String> adapter = new LettersAdapter(this, numbers);
        binding.gridView.setAdapter(adapter);
        binding.gridView.setOnItemClickListener((adapterView, view, i, l) -> {
            ItemGridLetterBinding itemBinding = ItemGridLetterBinding.bind(view);
            Toast.makeText(this, itemBinding.textLetter.getText(), Toast.LENGTH_SHORT).show();
        });
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "GridView");
            startActivity(intent);
        });
    }

    private static class LettersAdapter extends ArrayAdapter<String> {
        private final LayoutInflater inflater;

        LettersAdapter(@NonNull Context context, @NonNull String[] items) {
            super(context, 0, items);
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ItemGridLetterBinding itemBinding;
            if (convertView == null) {
                itemBinding = ItemGridLetterBinding.inflate(inflater, parent, false);
                convertView = itemBinding.getRoot();
                convertView.setTag(itemBinding);
            } else {
                itemBinding = (ItemGridLetterBinding) convertView.getTag();
            }
            String item = getItem(position);
            itemBinding.textLetter.setText(item != null ? item : "");
            return convertView;
        }
    }
}
