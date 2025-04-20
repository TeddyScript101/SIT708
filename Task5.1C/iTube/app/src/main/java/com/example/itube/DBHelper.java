package com.example.itube;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserDB";
    private static final int DB_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (username TEXT PRIMARY KEY, password TEXT)");
        db.execSQL("CREATE TABLE playlists (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, " +
                "youtube_url TEXT, " +
                "FOREIGN KEY(username) REFERENCES users(username))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("DROP TABLE IF EXISTS playlists");
            db.execSQL("CREATE TABLE playlists (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "username TEXT, " +
                    "youtube_url TEXT, " +
                    "FOREIGN KEY(username) REFERENCES users(username))");
        }
    }

    public boolean addUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);

        long result = db.insert("users", null, values);
        return result != -1;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username=?", new String[]{username});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    public boolean addToPlaylist(String username, String youtubeUrl) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM playlists WHERE username = ? AND youtube_url = ?",
                new String[]{username, youtubeUrl});
        boolean exists = cursor.moveToFirst();
        cursor.close();

        if (exists) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("youtube_url", youtubeUrl);

        long result = db.insert("playlists", null, values);
        return result != -1;
    }

    public List<String> getPlaylist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> playlist = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "SELECT youtube_url FROM playlists WHERE username = ?",
                new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex("youtube_url");
                if (columnIndex != -1) {
                    playlist.add(cursor.getString(columnIndex));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return playlist;
    }
}
