package com.example.do_it_app;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_it_app.Adapter.ToDoAdapter;
import com.example.do_it_app.Model.ToDoModel;
import com.example.do_it_app.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView tasksRecyclerView;
    private ActivityResultLauncher<Intent> newTaskLauncher;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    private List<ToDoModel> taskList;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.task);

        initializeDatabase();
        initializeViews();
        initializeRecyclerView();
        initializeActivityResultLauncher();
        loadTasksFromDB();

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, newTask.class);
            newTaskLauncher.launch(intent);
        });
    }

    private void initializeDatabase() {
        db = new DatabaseHandler(this);
        db.openDatabase();
        taskList = new ArrayList<>();
    }

    private void initializeViews() {
        fab = findViewById(R.id.fab);
        tasksRecyclerView = findViewById(R.id.tasksRecyclerView);
    }

    private void initializeRecyclerView() {
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(db, this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
    }

    private void initializeActivityResultLauncher() {
        newTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        loadTasksFromDB();
                    }
                }
        );
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        loadTasksFromDB();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void loadTasksFromDB() {
        taskList.clear();
        taskList.addAll(db.getAllTasks());
        if (taskList != null && !taskList.isEmpty()) {
            findViewById(R.id.no_tasks_message).setVisibility(View.GONE);
        } else {
            findViewById(R.id.no_tasks_message).setVisibility(View.VISIBLE);
        }
        Collections.reverse(taskList);
        tasksAdapter.setTasks(taskList);
        tasksAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasksFromDB();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_task_options, menu);
        if (menu instanceof MenuBuilder) {
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (id == R.id.action_delete_all) {
            confirmDeleteAllTasks();
            return true;
        } else if (id == R.id.action_delete_completed) {
            confirmDeleteCompletedTasks();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDeleteAllTasks() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa tất cả công việc")
                .setMessage("Bạn có chắc chắn muốn xóa tất cả công việc không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.deleteAllTasks();
                    Toast.makeText(this, "Đã xóa tất cả công việc", Toast.LENGTH_SHORT).show();
                    loadTasksFromDB();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void confirmDeleteCompletedTasks() {
        new AlertDialog.Builder(this)
                .setTitle("Xóa công việc đã hoàn thành")
                .setMessage("Bạn có chắc chắn muốn xóa tất cả công việc đã hoàn thành không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    db.deleteCompletedTasks();
                    Toast.makeText(this, "Đã xóa các công việc đã hoàn thành", Toast.LENGTH_SHORT).show();
                    loadTasksFromDB();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
