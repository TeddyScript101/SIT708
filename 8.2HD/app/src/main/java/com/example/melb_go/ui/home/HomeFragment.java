package com.example.melb_go.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melb_go.api.ApiService;
import com.example.melb_go.adapter.AttractionAdapter;
import com.example.melb_go.R;
import com.example.melb_go.api.RetrofitClient;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private AttractionAdapter adapter;
    private RecyclerView recyclerView;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        EditText searchEditText = root.findViewById(R.id.searchEditText);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchRunnable != null) {
                    searchHandler.removeCallbacks(searchRunnable);
                }
                searchRunnable = () -> {
                    String query = s.toString().trim();
                    homeViewModel.setSearchQuery(query);
                    adapter.clearAttractions();
                    homeViewModel.loadFirstPage();
                };
                searchHandler.postDelayed(searchRunnable, 1000); // debounce: 1 sec
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory(requireContext()))
                .get(HomeViewModel.class);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String token = getContext().getSharedPreferences("auth_prefs", getContext().MODE_PRIVATE)
                .getString("auth_token", null);
        adapter = new AttractionAdapter(getContext(), new ArrayList<>(), attractionId -> {
            Bundle bundle = new Bundle();
            bundle.putString("attraction_id", attractionId);
            Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_home_to_detailFragment, bundle);
        }, apiService, token);

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
            boolean isFirstLoad = true;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTheme = (String) parent.getItemAtPosition(position);
                if (isFirstLoad) {
                    isFirstLoad = false;
                    return;
                }
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

    @Override
    public void onResume() {
        super.onResume();
        homeViewModel.loadFirstPage();
    }
}
