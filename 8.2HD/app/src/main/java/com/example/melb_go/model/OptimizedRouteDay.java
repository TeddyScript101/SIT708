package com.example.melb_go.model;
import java.io.Serializable;
import java.util.List;

public class OptimizedRouteDay implements Serializable {
    public int day;
    public List<Place> route;
    public int total_travel_time_min;
    public int total_stay_time_min;
    public int total_time_min;
    public String summary;

    public static class Place implements Serializable {
        public int order;
        public String name;
        public double lat;
        public double lng;
        public int travel_time_from_previous_min;
        public int time_to_stay_min;
    }
}
