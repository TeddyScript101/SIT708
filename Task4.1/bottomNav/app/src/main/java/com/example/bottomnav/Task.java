package com.example.bottomnav;

import java.util.Date;

public class Task {

    private int id;
    private String taskTitle, taskDetail;
    private Date dueDate;

    public Task(int id, String taskTitle, String taskDetail, Date dueDate) {
        this.id = id;
        this.taskTitle = taskTitle;
        this.taskDetail = taskDetail;
        this.dueDate = dueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // ✅ This now returns the actual Date
    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }
}
