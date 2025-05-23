package com.example.melb_go.model;

import java.io.Serializable;
import java.util.List;

public class SaveRouteRequest implements Serializable {
    private String tripName;
    private List<OptimizedRouteDay> route;

    public SaveRouteRequest(String tripName, List<OptimizedRouteDay> route) {
        this.tripName = tripName;
        this.route = route;
    }

    public String getTripName() {
        return tripName;
    }

    public List<OptimizedRouteDay> getRoute() {
        return route;
    }
}
