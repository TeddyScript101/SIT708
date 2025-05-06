package com.example.lost_and_found;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "advert")
public class Advert {

    @PrimaryKey(autoGenerate = true)
    private int id; // Add a getter and setter for this field
    private String type;    // "Lost" or "Found"
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;

    // Constructor
    public Advert(String type, String name, String phone, String description, String date, String location) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id (if needed, usually Room handles this)
    public void setId(int id) {
        this.id = id;
    }

    // Getter for other fields
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
