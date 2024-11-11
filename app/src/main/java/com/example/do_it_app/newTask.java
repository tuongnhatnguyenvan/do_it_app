package com.example.do_it_app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_it_app.Model.ToDoModel;
import com.example.do_it_app.Utils.DatabaseHandler;

import java.util.Calendar;
import java.util.Objects;

public class newTask extends AppCompatActivity {

    private EditText taskTitle;
    private EditText detailText;
    private Spinner taskCategoriesSpinner;
    private TextView dateText;
    private TextView timeText;
    private Button saveButton;
    private boolean isEditMode = false;
    private int taskId;
    private DatabaseHandler dbHandler;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_task);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.new_task);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Initialize
        initializeViews();

        dateText.setOnClickListener(v -> showDatePickerDialog());
        timeText.setOnClickListener(v -> showTimePickerDialog());

        dbHandler = new DatabaseHandler(this);
        dbHandler.openDatabase();

        saveButton.setOnClickListener(v -> saveTask());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    dateText.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void initializeViews() {
        taskTitle = findViewById(R.id.taskTitle);
        detailText = findViewById(R.id.taskDetailsType);
        taskCategoriesSpinner = findViewById(R.id.spinner);
        dateText = findViewById(R.id.date_text);
        timeText = findViewById(R.id.time_text);
        saveButton = findViewById(R.id.save_button);
    }

    private void saveTask(){
        String task = taskTitle.getText().toString();
        String detail = detailText.getText().toString();
        String category = taskCategoriesSpinner.getSelectedItem().toString();
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();

        Log.d("new_TASK", "Task: " + task);
        Log.d("new_TASK", "Detail: " + detail);
        Log.d("new_TASK", "Category: " + category);
        Log.d("new_TASK", "Date: " + date);
        Log.d("new_TASK", "Time: " + time);

        if(isEditMode){
            dbHandler.updateTask(taskId,task,detail,category,date, time);
        }else {
            ToDoModel newTask = new ToDoModel(task, detail,category, date, time);
            dbHandler.insertTask(newTask);
        }
        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, selectedHour, selectedMinute) -> {
                    @SuppressLint("DefaultLocale") String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    timeText.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}