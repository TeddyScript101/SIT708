package com.example.bottomnav.ui.home;

import com.example.bottomnav.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bottomnav.RecyclerViewAdapter;
import com.example.bottomnav.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private RecyclerViewAdapter taskAdapter;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        taskAdapter = new RecyclerViewAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(taskAdapter);

        homeViewModel = new ViewModelProvider(
                this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().getApplication())
        ).get(HomeViewModel.class);

        homeViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            taskAdapter.setTasks(tasks);
        });

        Button sortButton = root.findViewById(R.id.btnSortByDate);
        sortButton.setOnClickListener(v -> {
            homeViewModel.toggleSortOrder();
            sortButton.setText(homeViewModel.isAscending()
                    ? "Sort by Date (ascending)"
                    : "Sort by Date (descending)");
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.loadTasks();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
