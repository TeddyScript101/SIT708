package com.example.melb_go.api;

import com.example.melb_go.model.OptimizedRouteDay;
import com.example.melb_go.model.TouristAttraction;

import java.util.List;

public class ApiResponse {
    private List<TouristAttraction> data;

    public List<TouristAttraction> getData() {
        return data;
    }

    private List<OptimizedRouteDay> route;

    public List<OptimizedRouteDay> getRoute() {
        return route;
    }

}
