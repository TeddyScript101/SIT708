package com.example.melb_go.ui.newPlanning;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melb_go.api.OptimizeRouteRequest;
import com.example.melb_go.R;
import com.example.melb_go.api.ApiResponse;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.api.RetrofitClient;
import com.example.melb_go.model.TouristAttraction;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.melb_go.adapter.ScheduleAttractionsAdapter;

public class NewPlanningFragment extends Fragment {

    private RecyclerView rvAttractions;
    private ScheduleAttractionsAdapter adapter;
    private TextView tvSelectedLocation;

    private ApiService apiService;
    private NewPlanningViewModel viewModel;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_planning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressBar = view.findViewById(R.id.progressBar);
        viewModel = new ViewModelProvider(requireActivity()).get(NewPlanningViewModel.class);

        Button btnSelectAttractions = view.findViewById(R.id.btnSelectAttractions);
        rvAttractions = view.findViewById(R.id.rvAttractions);
        tvSelectedLocation = view.findViewById(R.id.tvSelectedLocation);
        Button btnSelectStartingPoint = view.findViewById(R.id.btnSelectStartingPoint);
        Button btnOptimizeRoute = view.findViewById(R.id.btnOptimizeRoute);
        EditText etMaxJourneyHours = view.findViewById(R.id.etMaxJourneyHours);
        EditText etMaxJourneyMins = view.findViewById(R.id.etMaxJourneyMins);

        adapter = new ScheduleAttractionsAdapter(new ArrayList<>());
        rvAttractions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAttractions.setAdapter(adapter);

        btnSelectAttractions.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_newPlanningFragment_to_selectAttractionsFragment);
        });

        btnSelectStartingPoint.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_newPlanningFragment_to_mapPickLocationFragment);
        });

        btnOptimizeRoute.setOnClickListener(v -> {
            int maxHour = parseIntOrDefault(etMaxJourneyHours.getText().toString().trim(), 0);
            int maxMin = parseIntOrDefault(etMaxJourneyMins.getText().toString().trim(), 0);
            String token = requireContext()
                    .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                    .getString("auth_token", null);
            sendOptimizationRequest(token, maxHour, maxMin);
        });

        // Observe ViewModel
        viewModel.getAttractions().observe(getViewLifecycleOwner(), data -> {
            adapter.setData(data != null ? data : new ArrayList<>());
            adapter.notifyDataSetChanged();
        });

        viewModel.getSelectedLat().observe(getViewLifecycleOwner(), lat -> updateLocationDisplay());
        viewModel.getSelectedLng().observe(getViewLifecycleOwner(), lng -> updateLocationDisplay());

        // Fragment Result Listeners (only once)
        getParentFragmentManager().setFragmentResultListener("locationSelected", this, (requestKey, bundle) -> {
            double lat = bundle.getDouble("selectedLat");
            double lng = bundle.getDouble("selectedLng");
            viewModel.setSelectedLat(lat);
            viewModel.setSelectedLng(lng);
        });

        getParentFragmentManager().setFragmentResultListener("attractionSelection", this, (requestKey, bundle) -> {
            ArrayList<TouristAttraction> selectedAttractions =
                    (ArrayList<TouristAttraction>) bundle.getSerializable("selectedAttractions");
            List<ScheduleAttractionsAdapter.TouristAttractionWithTime> wrappedList = new ArrayList<>();
            for (TouristAttraction ta : selectedAttractions) {
                wrappedList.add(new ScheduleAttractionsAdapter.TouristAttractionWithTime(ta));
            }
            viewModel.setAttractions(wrappedList);
        });
    }

    private int parseIntOrDefault(String input, int defaultValue) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void updateLocationDisplay() {
        Double lat = viewModel.getSelectedLat().getValue();
        Double lng = viewModel.getSelectedLng().getValue();
        if (lat != null && lng != null) {
            String address = getAddressFromLatLng(lat, lng);
            tvSelectedLocation.setText(address != null ? address : "Lat: " + lat + ", Lng: " + lng);
        }
    }

    private String getAddressFromLatLng(double lat, double lng) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                return address.getAddressLine(0);
            }
        } catch (IOException e) {
            Log.e("NewPlanningFragment", "Geocoder failed", e);
        }
        return null;
    }


    private void sendOptimizationRequest(String token, int maxHour, int maxMin) {
        boolean useMockMode = false;

        Double lat = viewModel.getSelectedLat().getValue();
        Double lng = viewModel.getSelectedLng().getValue();

        if (!useMockMode && (lat == null || lng == null)) {
            Log.e("NewPlanningFragment", "Starting point not selected");
            return;
        }

        List<OptimizeRouteRequest.AttractionPayload> attractionsPayload = new ArrayList<>();

        if (!useMockMode) {
            for (ScheduleAttractionsAdapter.TouristAttractionWithTime item : adapter.getData()) {
                TouristAttraction ta = item.attraction;
                attractionsPayload.add(new OptimizeRouteRequest.AttractionPayload(
                        ta.getFeatureName(),
                        ta.getLat(),
                        ta.getLng(),
                        item.timeHours,
                        item.timeMinutes
                ));
            }
        }

        OptimizeRouteRequest.StartingPoint startingPoint = useMockMode
                ? new OptimizeRouteRequest.StartingPoint(0.0, 0.0) // dummy values
                : new OptimizeRouteRequest.StartingPoint(lat, lng);

        OptimizeRouteRequest requestPayload = new OptimizeRouteRequest(
                startingPoint,
                attractionsPayload,
                maxHour,
                maxMin
        );

        requestPayload.setMockMode(useMockMode);

        // Show loading spinner before API call
        progressBar.setVisibility(View.VISIBLE);

        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        apiService.optimizeRoute("Bearer " + token, requestPayload).enqueue(new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                progressBar.setVisibility(View.GONE); // hide on success
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NewPlanningFragment", "Route optimized successfully");

                    String jsonString = new Gson().toJson(response.body().getRoute());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("optimizedRoutes", jsonString);

                    NavHostFragment.findNavController(NewPlanningFragment.this)
                            .navigate(R.id.action_newPlanningFragment_to_optimizedRouteFragment, bundle);
                } else {
                    Log.e("NewPlanningFragment", "API error: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE); // hide on failure
                Log.e("NewPlanningFragment", "Request failed", t);
            }
        });
    }



    private retrofit2.Callback<ApiResponse> createCallback() {
        return new retrofit2.Callback<ApiResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ApiResponse> call, retrofit2.Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("NewPlanningFragment", "Route optimized successfully");

                    String jsonString = new Gson().toJson(response.body().getRoute());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("optimizedRoutes", jsonString);

                    NavHostFragment.findNavController(NewPlanningFragment.this)
                            .navigate(R.id.action_newPlanningFragment_to_optimizedRouteFragment, bundle);
                } else {
                    Log.e("NewPlanningFragment", "API error: " + response.code());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ApiResponse> call, Throwable t) {
                Log.e("NewPlanningFragment", "Request failed", t);
            }
        };
    }

}
