package com.example.do_it_app.Model;

public class ToDoModel {
    private int id, status;
    private String task;
    private String date;
    private String time;
    private String detail;
    private String task_categories;

    public ToDoModel() {
    }

    public ToDoModel(String task, String date, String time, String detail, String task_categories) {
        this.task = task;
        this.date = date;
        this.time = time;
        this.detail = detail;
        this.task_categories = task_categories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTask_category() {
        return task_categories;
    }

    public void setTask_category(String task_category) {
        this.task_categories = task_category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}
