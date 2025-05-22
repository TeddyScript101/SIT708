package com.example.melb_go.repository;


import android.util.Log;

import com.example.melb_go.api.ApiResponse;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.api.RetrofitClient;
import com.example.melb_go.model.TouristAttraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouristAttractionRepository {
    private String searchQuery = null;
    private final String token;

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String query) {
        this.searchQuery = query;
    }

    public interface CallbackListener {
        void onSuccess(List<TouristAttraction> data);

        void onFailure(Throwable t);
    }

    public TouristAttractionRepository(String token) {
        this.token = token;
    }

    public interface DetailCallbackListener {
        void onSuccess(TouristAttraction attraction);

        void onFailure(Throwable t);
    }

    public void fetchAttractions(String token, int page, String theme, CallbackListener listener) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Log.d("tokenDebug", token);
        if (token != null) {
            token = "Bearer " + token;
        }
        apiService.getAttractions(token, page, theme, searchQuery).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Repo", "Fetch successful: " + response.body().getData().size() + " items");
                    listener.onSuccess(response.body().getData());
                } else {
                    listener.onFailure(new Exception("API error or empty data"));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void getAttractionById(String attractionId, DetailCallbackListener listener) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        String authHeader = token != null ? "Bearer " + token : null;

        apiService.getAttractionById(attractionId, authHeader).enqueue(new Callback<TouristAttraction>() {
            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onSuccess(response.body());
                } else {
                    listener.onFailure(new Exception("Failed to load attraction details"));
                }
            }

            @Override
            public void onFailure(Call<TouristAttraction> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void fetchThemes(CallbackListener listener) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.getThemes().enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Repo", "Themes fetched: " + response.body().size());
                    //noinspection unchecked
                    listener.onSuccess((List) response.body());
                } else {
                    listener.onFailure(new Exception("API error or empty theme list"));
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }

    public void fetchBookmarks(CallbackListener listener) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        String authHeader = token != null ? "Bearer " + token : null;

        apiService.getAllBookmarks(authHeader).enqueue(new Callback<List<TouristAttraction>>() {
            @Override
            public void onResponse(Call<List<TouristAttraction>> call, Response<List<TouristAttraction>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("Repo", "Bookmarks fetched: " + response.body().size());
                    listener.onSuccess(response.body());
                } else {
                    Log.e("Repository", "Response error: " + response.code() + " " + response.message());
                    listener.onFailure(new Exception("Failed to fetch bookmarks"));
                }
            }

            @Override
            public void onFailure(Call<List<TouristAttraction>> call, Throwable t) {
                listener.onFailure(t);
            }
        });
    }



}
