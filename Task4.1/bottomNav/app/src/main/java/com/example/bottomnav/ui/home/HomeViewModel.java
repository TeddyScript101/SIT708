package com.example.bottomnav.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bottomnav.DBHelper;
import com.example.bottomnav.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private final MutableLiveData<List<Task>> taskList = new MutableLiveData<>();
    private final DBHelper dbHelper;

    private boolean isAscending = true;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        dbHelper = new DBHelper(application);
        loadTasks();
    }

    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }

    public void loadTasks() {
        ArrayList<Task> tasks = isAscending
                ? dbHelper.getAllTasksSortedByDateAsc()
                : dbHelper.getAllTasksSortedByDateDesc();

        taskList.setValue(tasks);
    }

    public void toggleSortOrder() {
        isAscending = !isAscending;
        loadTasks();
    }

    public boolean isAscending() {
        return isAscending;
    }
}
