package com.example.bottomnav;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TaskManager.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_TASKS = "tasks";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DETAIL = "detail";
    private static final String COLUMN_DUE_DATE = "due_date";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DETAIL + " TEXT, " +
                COLUMN_DUE_DATE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, task.getTaskTitle());
        values.put(COLUMN_DETAIL, task.getTaskDetail());

        if (task.getDueDate() != null) {
            values.put(COLUMN_DUE_DATE, dateFormat.format(task.getDueDate()));
        } else {
            values.putNull(COLUMN_DUE_DATE);
        }

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    private ArrayList<Task> getTasksSorted(String sortOrder) {
        ArrayList<Task> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_TASKS +
                " ORDER BY " +
                "CASE WHEN " + COLUMN_DUE_DATE + " IS NULL THEN 1 ELSE 0 END, " +
                COLUMN_DUE_DATE + " " + sortOrder;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String detail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DETAIL));
                String dueDateStr = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DUE_DATE));

                Date dueDate = null;
                try {
                    if (dueDateStr != null) {
                        dueDate = dateFormat.parse(dueDateStr);
                    }
                } catch (ParseException e) {
                    e.printStackTrace(); // you might want to log this
                }

                Task task = new Task(id, title, detail, dueDate);
                taskList.add(task);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return taskList;
    }


    public ArrayList<Task> getAllTasksSortedByDateAsc() {
        return getTasksSorted("ASC");
    }

    public ArrayList<Task> getAllTasksSortedByDateDesc() {
        return getTasksSorted("DESC");
    }


    public boolean updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, task.getTaskTitle());
        values.put(COLUMN_DETAIL, task.getTaskDetail());
        values.put(COLUMN_DUE_DATE, dateFormat.format(task.getDueDate())); // fixed

        int rows = db.update(TABLE_TASKS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(task.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete(TABLE_TASKS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }
}
