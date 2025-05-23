package com.example.melb_go.ui.selectAttractions;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melb_go.R;
import com.example.melb_go.model.TouristAttraction;
import com.example.melb_go.repository.TouristAttractionRepository;

import java.util.ArrayList;
import java.util.List;

import com.example.melb_go.adapter.SelectAttractionsAdapter;

public class SelectAttractionsFragment extends Fragment {

    private RecyclerView rvAttractions;
    private Button btnSubmit;
    private SelectAttractionsAdapter adapter;

    private TouristAttractionRepository repository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_attractions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String token = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("auth_token", null);

        repository = new TouristAttractionRepository(token);

        rvAttractions = view.findViewById(R.id.rvSelectAttractions);
        btnSubmit = view.findViewById(R.id.btnSubmitSelection);

        // Initialize adapter with empty list
        adapter = new SelectAttractionsAdapter(new ArrayList<>());
        rvAttractions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAttractions.setAdapter(adapter);

        fetchBookmarkedAttractions();

        btnSubmit.setOnClickListener(v -> {
            ArrayList<TouristAttraction> selected = new ArrayList<>(adapter.getSelectedItems());

            Bundle result = new Bundle();
            result.putSerializable("selectedAttractions", selected);

            getParentFragmentManager().setFragmentResult("attractionSelection", result);
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void fetchBookmarkedAttractions() {
        repository.fetchBookmarks(new TouristAttractionRepository.CallbackListener() {
            @Override
            public void onSuccess(List<TouristAttraction> data) {
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    adapter.setData(data);
                });
            }

            @Override
            public void onFailure(Throwable error) {
                // Handle failure here, e.g. log or show a message
                if (getActivity() == null) return;
                getActivity().runOnUiThread(() -> {
                    // You can show a toast, or simply clear adapter data
                    adapter.setData(new ArrayList<>());
                });
            }
        });
    }

}
