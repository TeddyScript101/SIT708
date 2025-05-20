package com.example.melb_go;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttractionDetailFragment extends Fragment {

    private static final String ARG_ATTRACTION_ID = "attraction_id";
    private String attractionId;

    private ImageView imageView;
    private TextView nameTextView, themeTextView, coordTextView;

    public static AttractionDetailFragment newInstance(String id) {
        AttractionDetailFragment fragment = new AttractionDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ATTRACTION_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            attractionId = getArguments().getString(ARG_ATTRACTION_ID);
            Log.d("AttractionDetail", "Received attractionId: " + attractionId);
        } else {
            Log.e("AttractionDetail", "Arguments are null!");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_attraction_detail, container, false);

        imageView = view.findViewById(R.id.detailImage);
        nameTextView = view.findViewById(R.id.detailName);
        themeTextView = view.findViewById(R.id.detailTheme);
        coordTextView = view.findViewById(R.id.detailCoordinates);

        loadAttractionById();

        return view;
    }

    private void loadAttractionById() {
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<TouristAttraction> call = apiService.getAttractionById(attractionId);

        Log.d("attractionId", attractionId != null ? attractionId : "attractionId is null");
        call.enqueue(new Callback<TouristAttraction>() {
            @Override
            public void onResponse(Call<TouristAttraction> call, Response<TouristAttraction> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TouristAttraction attraction = response.body();
                    nameTextView.setText(attraction.getFeatureName());
                    themeTextView.setText(attraction.getTheme() + " - " + attraction.getSubTheme());
                    coordTextView.setText("Coordinates: " + attraction.getCoordinates());
                    Glide.with(requireContext()).load(attraction.getImageUrl()).into(imageView);
                }
            }

            @Override
            public void onFailure(Call<TouristAttraction> call, Throwable t) {
                nameTextView.setText("Failed to load details.");
            }
        });
    }
}
