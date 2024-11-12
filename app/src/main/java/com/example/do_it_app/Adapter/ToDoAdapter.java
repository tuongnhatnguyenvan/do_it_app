package com.example.do_it_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_it_app.MainActivity;
import com.example.do_it_app.Model.ToDoModel;
import com.example.do_it_app.R;
import com.example.do_it_app.Utils.DatabaseHandler;
import com.example.do_it_app.newTask;
//import com.google.gson.Gson;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    private List<ToDoModel> todoList;
    private final MainActivity activity;
    private final DatabaseHandler db;

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

//        Gson gson = new Gson();
//        String json = gson.toJson(item);
//        Log.d("Edit Item debug: ", json);

        Intent intent = new Intent(activity, newTask.class);

        intent.putExtra("id", item.getId());
        intent.putExtra("task", item.getTask());
        intent.putExtra("detail", item.getDetail());
        intent.putExtra("date", item.getDate());
        intent.putExtra("time", item.getTime());
        intent.putExtra("task_category", item.getTask_category());

        activity.startActivity(intent);

    }
}
