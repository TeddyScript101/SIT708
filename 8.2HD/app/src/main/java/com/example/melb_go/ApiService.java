package com.example.melb_go;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api/getThemeType")
    Call<List<String>> getThemes();

    @GET("/api/data")
    Call<ApiResponse> getAttractions(
            @Query("page") int page,
            @Query("theme") String theme
    );

}
