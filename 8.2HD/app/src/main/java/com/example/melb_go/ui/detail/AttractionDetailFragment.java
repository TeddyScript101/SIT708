package com.example.melb_go.ui.detail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.melb_go.BookmarkManager;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.R;
import com.example.melb_go.api.RetrofitClient;
import com.example.melb_go.model.TouristAttraction;
import com.example.melb_go.repository.TouristAttractionRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttractionDetailFragment extends Fragment {

    private static final String ARG_ATTRACTION_ID = "attraction_id";
    private String attractionId;

    private ImageView imageView;
    private TextView nameTextView, themeTextView, coordTextView;
    private WebView mapWebView;

    private ImageView bookmarkIcon;


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
        mapWebView = view.findViewById(R.id.mapWebView);
        bookmarkIcon = view.findViewById(R.id.bookmarkIcon);

        WebSettings webSettings = mapWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        loadAttractionById();

        return view;
    }

    private void loadAttractionById() {
        String token = requireContext()
                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                .getString("auth_token", null);

        TouristAttractionRepository repository = new TouristAttractionRepository(token);
        repository.getAttractionById(attractionId, new TouristAttractionRepository.DetailCallbackListener() {
            @Override
            public void onSuccess(TouristAttraction attraction) {
                requireActivity().runOnUiThread(() -> {
                    boolean isBookmarked = attraction.isBookmarked();
                    bookmarkIcon.setImageResource(
                            isBookmarked ? R.drawable.filledheart : R.drawable.heart
                    );

                    bookmarkIcon.setOnClickListener(v -> {
                        String token = requireContext()
                                .getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                                .getString("auth_token", null);

                        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

                        BookmarkManager.toggleBookmark(
                                requireContext(),
                                attraction,
                                apiService,
                                token,
                                true, // showToast
                                () -> {
                                    // Success – update heart icon based on new state
                                    bookmarkIcon.setImageResource(
                                            attraction.isBookmarked() ? R.drawable.filledheart : R.drawable.heart
                                    );
                                },
                                () -> {
                                    // Failure – optionally handle UI changes or retry
                                }
                        );
                    });

                    double lat = attraction.getLat().doubleValue();
                    double lng = attraction.getLng().doubleValue();

                    nameTextView.setText(attraction.getFeatureName());
                    themeTextView.setText(attraction.getTheme() + " - " + attraction.getSubTheme());
                    coordTextView.setText("📍 Location:\nLatitude: " + lat + "\nLongitude: " + lng);

                    Glide.with(requireContext()).load(attraction.getImageUrl()).into(imageView);

                    String mapHtml = "<iframe width=\"100%\" height=\"100%\" style=\"border:0\" loading=\"lazy\" allowfullscreen "
                            + "src=\"https://maps.google.com/maps?q=" + lat + "," + lng +
                            "&z=15&output=embed&markers=color:red%7C" + lat + "," + lng + "\"></iframe>";

                    mapWebView.loadData(mapHtml, "text/html", "utf-8");
                });
            }

            @Override
            public void onFailure(Throwable t) {
                requireActivity().runOnUiThread(() -> {
                    nameTextView.setText("Failed to load details.");
                    Log.e("AttractionDetail", "API call failed: " + t.getMessage());
                });
            }
        });

    }
}
