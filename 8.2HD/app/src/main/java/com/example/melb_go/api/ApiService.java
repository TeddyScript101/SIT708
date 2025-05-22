package com.example.melb_go.api;

import com.example.melb_go.model.TouristAttraction;
import com.example.melb_go.ui.Settings.AuthResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/api/getThemeType")
    Call<List<String>> getThemes();

    @GET("/api/data")
    Call<ApiResponse> getAttractions(
            @Header("Authorization") String token,
            @Query("page") int page,
            @Query("theme") String theme,
            @Query("search") String search
    );

    @GET("/api/data/{id}")
    Call<TouristAttraction> getAttractionById(
            @Path("id") String id,
            @Header("Authorization") String token
    );

    @GET("/api/bookmark")
    Call<List<TouristAttraction>> getAllBookmarks(
            @Header("Authorization") String token
    );

    @POST("/api/bookmark/{id}")
    Call<Void> bookmarkAttraction(
            @Path("id") String attractionId,
            @Header("Authorization") String token
    );


    @POST("/api/unbookmark/{id}")
    Call<Void> unbookmarkAttraction(
            @Path("id") String attractionId,
            @Header("Authorization") String token
    );

    @POST("/api/login")
    Call<AuthResponse> login(@Body com.example.melb_go.AuthRequest request);

    @POST("/api/signup")
    Call<AuthResponse> signup(@Body com.example.melb_go.AuthRequest request);
}
