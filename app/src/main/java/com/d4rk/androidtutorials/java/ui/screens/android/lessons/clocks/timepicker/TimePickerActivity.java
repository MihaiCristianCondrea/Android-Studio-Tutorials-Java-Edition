package com.d4rk.androidtutorials.java.ui.screens.android.lessons.clocks.timepicker;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.d4rk.androidtutorials.java.databinding.ActivityTimePickerBinding;
import com.d4rk.androidtutorials.java.ui.components.navigation.SyntaxFabActivity;
import com.d4rk.androidtutorials.java.ui.screens.android.CodeActivity;
import com.d4rk.androidtutorials.java.utils.EdgeToEdgeHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerActivity extends SyntaxFabActivity {
    private final Calendar calendar = Calendar.getInstance();
    private ActivityTimePickerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTimePickerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdgeHelper.applyEdgeToEdge(getWindow(), binding.getRoot());
        updateTimeInView();
        binding.changeTimeButton.setOnClickListener(v -> {
            TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                updateTimeInView();
            };
            new TimePickerDialog(TimePickerActivity.this,
                    timeSetListener,
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true).show();
        });
        setupSyntaxFab(binding.floatingButtonShowSyntax, () -> {
            Intent intent = new Intent(this, CodeActivity.class);
            intent.putExtra("lesson_name", "TimePicker");
            startActivity(intent);
        });
    }

    private void updateTimeInView() {
        String timeFormat = "HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        binding.timeTextView.setText(sdf.format(calendar.getTime()));
    }
}
