package com.example.lost_and_found;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "advert")
public class Advert {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    private String name;
    private String phone;
    private String description;
    private String date;
    private String location;

    private double latitude;
    private double longitude;

    // Constructor
    public Advert(String type, String name, String phone, String description, String date, String location,double latitude, double longitude) {
        this.type = type;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
