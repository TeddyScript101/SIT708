package com.example.melb_go.ui.optimizedRoute;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.melb_go.adapter.RouteAdapter;
import com.example.melb_go.api.ApiService;

import com.example.melb_go.api.RetrofitClient;
import com.example.melb_go.databinding.FragmentOptimizedRouteBinding;
import com.example.melb_go.model.OptimizedRouteDay;
import com.example.melb_go.model.SaveRouteRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OptimizedRouteFragment extends Fragment {

    private FragmentOptimizedRouteBinding binding;
    private List<OptimizedRouteDay> dayRoutes;
    private ApiService apiService;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOptimizedRouteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String token = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("auth_token", null);

        binding.routeRecyclerView.setHasFixedSize(true);
        binding.routeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        if (getArguments() != null) {
            String json = getArguments().getString("optimizedRoutes");
            if (json != null) {
                try {
                    Type listType = new TypeToken<List<OptimizedRouteDay>>() {}.getType();
                    dayRoutes = new Gson().fromJson(json, listType);

                    RouteAdapter adapter = new RouteAdapter(dayRoutes);
                    binding.routeRecyclerView.setAdapter(adapter);

                } catch (Exception e) {
                    Log.e("OptimizedRouteFragment", "Failed to parse JSON", e);
                    Toast.makeText(getContext(), "Error loading route data", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // Set click listener for Save button
        binding.submitButton.setOnClickListener(v -> {
            String tripName = binding.tripNameEditText.getText().toString().trim();
            if (tripName.isEmpty()) {
                binding.tripNameEditText.setError("Trip name required");
                binding.tripNameEditText.requestFocus();
                return;
            }

            if (dayRoutes == null || dayRoutes.isEmpty()) {
                Toast.makeText(getContext(), "No route data to save", Toast.LENGTH_SHORT).show();
                return;
            }

            SaveRouteRequest request = new SaveRouteRequest(tripName, dayRoutes);
            apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
            Call<Void> call = apiService.saveRoute("Bearer " + token, request);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Route saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Failed to save route: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
