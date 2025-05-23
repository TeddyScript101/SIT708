package com.example.melb_go.model;

import java.util.List;

public class RoutesResponse {
    private List<SaveRouteRequest> routes;

    public List<SaveRouteRequest> getRoutes() {
        return routes;
    }

    public void setRoutes(List<SaveRouteRequest> routes) {
        this.routes = routes;
    }
}
