package com.example.self_learning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "users_db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_USERS_TABLE = "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, email TEXT, password TEXT, phone TEXT, avatar BLOB)";
        db.execSQL(CREATE_USERS_TABLE);


        String CREATE_INTERESTS_TABLE = "CREATE TABLE interests (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, interest TEXT, FOREIGN KEY(username) REFERENCES users(username))";
        db.execSQL(CREATE_INTERESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS interests");
        onCreate(db);
    }


    public boolean insertUser(String username, String email, String password, String phone, byte[] avatar, ArrayList<String> interests) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Insert user details
        ContentValues userValues = new ContentValues();
        userValues.put("username", username);
        userValues.put("email", email);
        userValues.put("password", password);
        userValues.put("phone", phone);
        userValues.put("avatar", avatar);


        long result = db.insert("users", null, userValues);

        if (result == -1) {
            db.close();
            return false;
        }


        for (String interest : interests) {
            ContentValues interestValues = new ContentValues();
            interestValues.put("username", username);  // Use username instead of user_id
            interestValues.put("interest", interest);


            db.insert("interests", null, interestValues);
        }

        db.close();
        return true; // Success
    }


    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username, password});

        boolean isValid = cursor != null && cursor.getCount() > 0;
        cursor.close();

        return isValid;
    }


    public ArrayList<String> getUserInterests(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT interest FROM interests WHERE username = ?", new String[]{username});

        ArrayList<String> interests = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            int interestColumnIndex = cursor.getColumnIndex("interest");

            if (interestColumnIndex != -1) {
                do {
                    interests.add(cursor.getString(interestColumnIndex));
                } while (cursor.moveToNext());
            } else {
                Log.e("DBHelper", "Column 'interest' not found.");
            }
        }
        cursor.close();
        return interests; // Return an empty list if no data found
    }

}
