package com.example.melb_go.api;

import java.util.List;

// Define a POJO class for the request payload (recommended for type safety)
public class OptimizeRouteRequest {
    public StartingPoint startingPoint;
    public List<AttractionPayload> attractions;
    public int maxHour;
    public int maxMin;
    private boolean mockMode;

    public void setMockMode(boolean mockMode) {
        this.mockMode = mockMode;
    }

    public static class StartingPoint {
        public double lat;
        public double lng;
        public StartingPoint(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

    public static class AttractionPayload {
        public String name;
        public Number lat;
        public Number lng;

        public int timeToStayInHour;
        public int timeToStayInMin;

        public AttractionPayload(String name, Number lat, Number lng, int timeToStayInHour, int timeToStayInMin) {
            this.name = name;
            this.lat = lat;
            this.lng = lng;
            this.timeToStayInHour = timeToStayInHour;
            this.timeToStayInMin = timeToStayInMin;
        }
    }

    public OptimizeRouteRequest(StartingPoint startingPoint, List<AttractionPayload> attractions, int maxHour, int maxMin) {
        this.startingPoint = startingPoint;
        this.attractions = attractions;
        this.maxHour = maxHour;
        this.maxMin = maxMin;
    }
}

