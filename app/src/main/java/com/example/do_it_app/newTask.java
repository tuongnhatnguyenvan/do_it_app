package com.example.do_it_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class newTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_task);

//        if (getSupportActionBar() != null) {
//            Objects.requireNonNull(getSupportActionBar()).hide();
//        }
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.new_task);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize buttons
        Button dateButton = findViewById(R.id.date_button);
        Button timeButton = findViewById(R.id.time_button);

        // Get the current date and time
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        // Date picker
        dateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (view, selectedYear, selectedMonth, selectedDayOfMonth) -> {
                        // Handle date selection here
                    },
                    year, month, day
            );
            datePickerDialog.show();
        });

        // Time picker
        timeButton.setOnClickListener(v -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    this,
                    (view, selectedHour, selectedMinute) -> {
                        // Handle time selection here
                    },
                    hour, minute, true
            );
            timePickerDialog.show();
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}