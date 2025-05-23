package com.example.melb_go.ui.planDetail;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.melb_go.adapter.RouteAdapter;
import com.example.melb_go.databinding.FragmentPlanDetailBinding;
import com.example.melb_go.model.OptimizedRouteDay;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PlanDetailFragment extends Fragment {

    private FragmentPlanDetailBinding binding;
    private RouteAdapter routeAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPlanDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.routeRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Bundle args = getArguments();
        if (args != null && args.containsKey("optimizedRoutes")) {
            String routeJson = args.getString("optimizedRoutes");
            List<OptimizedRouteDay> dayRoutes = parseJsonToRouteList(routeJson);
            routeAdapter = new RouteAdapter(dayRoutes);
            binding.routeRecyclerView.setAdapter(routeAdapter);

            binding.routeRecyclerView.setAdapter(routeAdapter);
        }

        return view;
    }

    private List<OptimizedRouteDay> parseJsonToRouteList(String json) {
        Type listType = new TypeToken<List<OptimizedRouteDay>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
