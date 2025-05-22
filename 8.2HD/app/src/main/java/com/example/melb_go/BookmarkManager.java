package com.example.melb_go;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.melb_go.api.ApiService;
import com.example.melb_go.model.TouristAttraction;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarkManager {

    // Functional interface for callbacks
    public interface SimpleCallback {
        void call();
    }

    public static void toggleBookmark(
            Context context,
            TouristAttraction attraction,
            ApiService apiService,
            String token,
            boolean showToast,
            SimpleCallback onSuccess,
            SimpleCallback onFailure
    ) {
        boolean currentlyBookmarked = attraction.isBookmarked();
        String bearerToken = "Bearer " + token;

        if (!currentlyBookmarked) {
            apiService.bookmarkAttraction(attraction.getId(), bearerToken)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                attraction.setBookmarked(true);
                                if (showToast) {
                                    Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show();
                                }
                                if (onSuccess != null) onSuccess.call();
                            } else {
                                Toast.makeText(context, "Login required to bookmark", Toast.LENGTH_SHORT).show();
                                if (onFailure != null) onFailure.call();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Failed to bookmark", Toast.LENGTH_SHORT).show();
                            Log.e("BookmarkManager", "Bookmark failed", t);
                            if (onFailure != null) onFailure.call();
                        }
                    });
        } else {
            apiService.unbookmarkAttraction(attraction.getId(), bearerToken)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                attraction.setBookmarked(false);
                                if (showToast) {
                                    Toast.makeText(context, "Unbookmarked!", Toast.LENGTH_SHORT).show();
                                }
                                if (onSuccess != null) onSuccess.call();
                            } else {
                                Toast.makeText(context, "Failed to unbookmark", Toast.LENGTH_SHORT).show();
                                if (onFailure != null) onFailure.call();
                            }
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(context, "Failed to unbookmark", Toast.LENGTH_SHORT).show();
                            Log.e("BookmarkManager", "Unbookmark failed", t);
                            if (onFailure != null) onFailure.call();
                        }
                    });
        }
    }
}
