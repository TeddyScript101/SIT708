package com.example.melb_go.model;

import com.google.gson.annotations.SerializedName;

public class TouristAttraction {

    @SerializedName("_id")
    private String id;

    @SerializedName("theme")
    private String theme;

    @SerializedName("subTheme")
    private String subTheme;

    @SerializedName("name")
    private String featureName;

    @SerializedName("lat")
    private Number lat;

    @SerializedName("lng")
    private Number lng;

    @SerializedName("image")
    private String imageUrl;
    @SerializedName("isBookmarked")
    private boolean bookmarked;

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public TouristAttraction() {
    }

    public TouristAttraction(String id, String theme, String subTheme, String featureName, Number lat, Number lng, String imageUrl) {
        this.id = id;
        this.theme = theme;
        this.subTheme = subTheme;
        this.featureName = featureName;
        this.lat = lat;
        this.lng = lng;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getTheme() {
        return theme;
    }

    public String getSubTheme() {
        return subTheme;
    }

    public String getFeatureName() {
        return featureName;
    }

    public Number getLng() {
        return lng;
    }

    public Number getLat() {
        return lat;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
