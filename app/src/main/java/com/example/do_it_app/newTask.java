package com.example.do_it_app;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.do_it_app.Model.ToDoModel;
import com.example.do_it_app.Utils.DatabaseHandler;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class newTask extends AppCompatActivity {

    private EditText taskTitleEditText;
    private EditText taskDetailsEditText;
    private Spinner taskCategorySpinner;
    private TextView dateTextView;
    private TextView timeTextView;
    private Button saveButton;
    private boolean isEditMode = false;
    private int taskId;
    private DatabaseHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_task);

        setupActionBar();
        initializeViews();
        setupListeners();

        dbHandler = new DatabaseHandler(this);
        dbHandler.openDatabase();

        handleIntentData();
    }

    private void setupActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.new_task);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeViews() {
        taskTitleEditText = findViewById(R.id.taskTitle);
        taskDetailsEditText = findViewById(R.id.taskDetailsType);
        taskCategorySpinner = findViewById(R.id.spinner);
        dateTextView = findViewById(R.id.date_text);
        timeTextView = findViewById(R.id.time_text);
        saveButton = findViewById(R.id.save_button);
    }

    private void setupListeners() {
        dateTextView.setOnClickListener(v -> showDatePickerDialog());
        timeTextView.setOnClickListener(v -> showTimePickerDialog());
        saveButton.setOnClickListener(v -> saveTask());
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra("id")) return;

        isEditMode = true;
        taskId = intent.getIntExtra("id", -1);
        taskTitleEditText.setText(intent.getStringExtra("task"));
        taskDetailsEditText.setText(intent.getStringExtra("detail"));
        dateTextView.setText(intent.getStringExtra("date"));
        timeTextView.setText(intent.getStringExtra("time"));
        setCategorySelection(intent.getStringExtra("task_category"));
    }

    private void setCategorySelection(String taskCategory) {
        String[] categories = getResources().getStringArray(R.array.task_categories);
        for (int i = 0; i < categories.length; i++) {
            if (categories[i].equals(taskCategory)) {
                taskCategorySpinner.setSelection(i);
                break;
            }
        }
    }

    private void saveTask() {
        String taskTitle = taskTitleEditText.getText().toString().trim();
        String taskDetails = taskDetailsEditText.getText().toString().trim();
        String category = taskCategorySpinner.getSelectedItem().toString();
        String date = dateTextView.getText().toString();
        String time = timeTextView.getText().toString();

        if (taskTitle.isEmpty()) {
            taskTitleEditText.setError("Vui lòng nhập tiêu đề cho công việc");
            taskTitleEditText.requestFocus();
            return;
        }

        if (date.isEmpty() || date.equals("Set Date")) {
            date = getCurrentDate();
            dateTextView.setText(date);
        }

        if (time.isEmpty() || time.equals("Set Time")) {
            time = getCurrentTime();
            timeTextView.setText(time);
        }

        if (isEditMode) {
            dbHandler.updateTask(taskId, taskTitle, taskDetails, category, date, time);
        } else {
            ToDoModel newTask = new ToDoModel(taskTitle, date, time, taskDetails, category);
            dbHandler.insertTask(newTask);
        }

        setResult(RESULT_OK, new Intent());
        finish();
    }

    @SuppressLint("DefaultLocale")
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this,
                R.style.CustomDatePicker,
                (view, year, month, dayOfMonth) ->
                        dateTextView.setText(String.format("%d/%d/%d", dayOfMonth, month + 1, year)),
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @SuppressLint("DefaultLocale")
    private void showTimePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this,
                R.style.CustomTimePicker,
                (view, hourOfDay, minute) ->
                        timeTextView.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                .show();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return timeFormat.format(Calendar.getInstance().getTime());
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}