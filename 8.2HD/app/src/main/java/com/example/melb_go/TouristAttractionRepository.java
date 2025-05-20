package com.example.melb_go;


import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TouristAttractionRepository {
    private String searchQuery = null;

    public void setSearchQuery(String query) {
        this.searchQuery = query;
    }

    public interface CallbackListener {
        void onSuccess(List<TouristAttraction> data);

        void onFailure(Throwable t);
    }

    public void fetchAttractions(int page, String theme, CallbackListener listener) {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

        // Call Retrofit method with theme param (can be null)
        apiService.getAttractions(page, theme,searchQuery).enqueue(new Callback<ApiResponse>() {
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


}
