package com.example.lost_and_found;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AdvertDao {
    @Insert
    void insert(Advert advert);

    @Query("SELECT * FROM Advert")
    List<Advert> getAllAdverts();

    @Query("SELECT * FROM Advert WHERE id = :id")
    Advert getById(int id);

    @Query("DELETE FROM Advert WHERE id = :id")
    void deleteById(int id);
}
