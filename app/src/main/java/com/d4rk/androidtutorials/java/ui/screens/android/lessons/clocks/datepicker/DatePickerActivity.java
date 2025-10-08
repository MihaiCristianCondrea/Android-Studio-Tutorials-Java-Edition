package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.datepicker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import com.d4rk.androidtutorials.java.databinding.ActivityDatePickerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DatePickerActivity extends SyntaxFabActivity {
    private final Calendar calendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private ActivityDatePickerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());


        updateDateInView();
        DatePickerDialog.OnDateSetListener dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateInView();
        };
        binding.changeDateButton.setOnClickListener(v -> new DatePickerDialog(DatePickerActivity.this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show());
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "DatePicker");
            startActivity(intent);
        });
    }

    private void updateDateInView() {
        binding.dateTextView.setText(dateFormat.format(calendar.getTime()));
    }
}
