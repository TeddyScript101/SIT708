package com.example.melb_go.ui.routeAssistant;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.melb_go.R;

public class RouteAssistantFragment extends Fragment {

    public RouteAssistantFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_route_assistant, container, false);

        root.findViewById(R.id.startPlanningBox).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_routeAssistantFragment_to_newPlanningFragment);
        });
        root.findViewById(R.id.retrievePlanningBox).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_routeAssistantFragment_to_planListFragment);
        });

        return root;
    }
}
