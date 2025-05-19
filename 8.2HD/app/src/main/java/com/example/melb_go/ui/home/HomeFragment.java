package com.example.melb_go.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melb_go.AttractionAdapter;
import com.example.melb_go.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private AttractionAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AttractionAdapter(getContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);

        Spinner themeSpinner = root.findViewById(R.id.themeSpinner);

        homeViewModel.getThemeList().observe(getViewLifecycleOwner(), themes -> {
            if (themes != null && !themes.isEmpty()) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_spinner_item, themes);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                themeSpinner.setAdapter(spinnerAdapter);

                themeSpinner.setSelection(0);
            }
        });

        homeViewModel.loadThemes();

        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = (String) parent.getItemAtPosition(position);

                homeViewModel.setSelectedTheme(selectedTheme);

                adapter.clearAttractions();

                homeViewModel.loadFirstPage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        homeViewModel.getAttractionList().observe(getViewLifecycleOwner(), data -> {
            if (data != null) {
                int oldSize = adapter.getItemCount();
                int newSize = data.size();
                if (newSize > oldSize) {
                    adapter.addAttractions(data.subList(oldSize, newSize));
                }
            }
        });

        // Add scroll listener for pagination
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if (!homeViewModel.isLoading() && !homeViewModel.isLastPage() &&
                            (visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 3 &&
                            firstVisibleItemPosition >= 0) {
                        homeViewModel.loadNextPage();
                    }
                }
            }
        });

        return root;
    }

}
