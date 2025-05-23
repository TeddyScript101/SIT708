package com.example.melb_go.ui.planList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.melb_go.R;
import com.example.melb_go.adapter.RouteAdapter;
import com.example.melb_go.adapter.TripListAdapter;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.api.RetrofitClient;
import com.example.melb_go.databinding.FragmentPlanListBinding;
import com.example.melb_go.model.RoutesResponse;
import com.example.melb_go.model.SaveRouteRequest;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanListFragment extends Fragment {
    private FragmentPlanListBinding binding;
    private ApiService apiService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlanListBinding.inflate(inflater, container, false);
        apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.planRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchPlans();
    }

    private void fetchPlans() {
        String token = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("auth_token", null);

        apiService.getSavedRoutes("Bearer " + token).enqueue(new Callback<RoutesResponse>() {
            @Override
            public void onResponse(@NonNull Call<RoutesResponse> call, @NonNull Response<RoutesResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SaveRouteRequest> routes = response.body().getRoutes();

                    TripListAdapter adapter = new TripListAdapter(routes, routeData -> {
                        String json = new Gson().toJson(routeData.getRoute());

                        Bundle bundle = new Bundle();
                        bundle.putString("optimizedRoutes", json);

                        NavHostFragment.findNavController(PlanListFragment.this)
                                .navigate(R.id.action_planList_to_planDetail, bundle);
                    });

                    binding.planRecyclerView.setAdapter(adapter);

                } else {
                    Log.e("PlanListFragment", "Failed to parse response: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<RoutesResponse> call, @NonNull Throwable t) {
                Log.e("PlanListFragment", "Error: " + t.getMessage(), t);
            }
        });
    }

}
