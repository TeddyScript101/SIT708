package com.example.melb_go;

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

    @SerializedName("co_ordinates")
    private String coordinates;

    @SerializedName("image")
    private String imageUrl;

    public TouristAttraction() {
    }

    public TouristAttraction(String id, String theme, String subTheme, String featureName, String coordinates, String imageUrl) {
        this.id = id;
        this.theme = theme;
        this.subTheme = subTheme;
        this.featureName = featureName;
        this.coordinates = coordinates;
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

    public String getCoordinates() {
        return coordinates;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
