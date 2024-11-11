package com.example.do_it_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_it_app.AddNewTask;
import com.example.do_it_app.MainActivity;
import com.example.do_it_app.Model.ToDoModel;
import com.example.do_it_app.R;
import com.example.do_it_app.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private MainActivity activity;
    private DatabaseHandler db;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db.openDatabase();
        final ToDoModel item = todoList.get(position);
        holder.taskTitle.setText(item.getTask());
        holder.task.setChecked(toBoolean(item.getStatus()));
        holder.detail.setText(item.getDetail());
        holder.taskCategories.setText(item.getTask_category());
        holder.dateTextView.setText(item.getDate());
        holder.timeTextView.setText(item.getTime());
        holder.task.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                db.updateStatus(item.getId(), 1);
            } else {
                db.updateStatus(item.getId(), 0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox task;
        TextView taskTitle;
        TextView dateTextView;
        TextView timeTextView;
        TextView detail;
        TextView taskCategories;

        ViewHolder(View view) {
            super(view);
            task = view.findViewById(R.id.todoCheckBox);
            taskTitle = view.findViewById(R.id.taskTitle);
            detail = view.findViewById(R.id.taskDetails);
            taskCategories = view.findViewById(R.id.taskCategory);
            dateTextView = view.findViewById(R.id.reminderDate);
            timeTextView = view.findViewById(R.id.reminderTime);
        }
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("detail", item.getDetail());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        bundle.putString("task_category", item.getTask_category());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }
}
