package com.example.lost_and_found;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Advert.class}, version = 2)
public abstract class AdvertDatabase extends RoomDatabase {
    public abstract AdvertDao advertDao();
}
