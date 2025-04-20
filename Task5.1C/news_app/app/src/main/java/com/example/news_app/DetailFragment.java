package com.example.news_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private static final String ARG_TITLE = "title";
    private static final String ARG_IMAGE_URL = "image_url";
    private static final String ARG_DESCRIPTION = "description";

    private String title;
    private String imageUrl;
    private String description;

    private RecyclerView relatedRecyclerView;

    public static DetailFragment newInstance(NewsItem item) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, item.getTitle());
        args.putString(ARG_IMAGE_URL, item.getImageUrl());
        args.putString(ARG_DESCRIPTION, item.getDescription());
        fragment.setArguments(args);
        return fragment;
    }

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            imageUrl = getArguments().getString(ARG_IMAGE_URL);
            description = getArguments().getString(ARG_DESCRIPTION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        ImageView image = view.findViewById(R.id.detail_image);
        TextView titleView = view.findViewById(R.id.detail_title);
        TextView descView = view.findViewById(R.id.detail_description);
        relatedRecyclerView = view.findViewById(R.id.recycler_related_news);

        Glide.with(requireContext()).load(imageUrl).into(image);
        titleView.setText(title);
        descView.setText(description);

        setupRelatedRecyclerView();

        Button backButton = view.findViewById(R.id.button_back_home);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment()) // Replace with your container ID
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void updateDetailContent(NewsItem item) {
        this.title = item.getTitle();
        this.imageUrl = item.getImageUrl();
        this.description = item.getDescription();

        View view = getView();
        if (view != null) {
            TextView titleView = view.findViewById(R.id.detail_title);
            TextView descView = view.findViewById(R.id.detail_description);
            ImageView image = view.findViewById(R.id.detail_image);

            titleView.setText(title);
            descView.setText(description);
            Glide.with(requireContext()).load(imageUrl).into(image);
        }
    }

    private void setupRelatedRecyclerView() {
        List<NewsItem> relatedNewsList = new ArrayList<>();
        relatedNewsList.add(new NewsItem("Related News 1", "https://imageresizer.static9.net.au/EObULjdDid8nqUO-Mjmswd7jnOY=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F286dbb32-21ae-4b9e-980c-6f716e091beb", "General news description 1"));
        relatedNewsList.add(new NewsItem("Related News 2", "https://imageresizer.static9.net.au/01bwVWjgZlt-AbMxlHB_TqWPSUA=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F02fc579e-476c-4614-9a94-8b3bfaec31fb", "General news description 2"));
        relatedNewsList.add(new NewsItem("Related News 3", "https://imageresizer.static9.net.au/8tnD4AgY07MUVabyqqwJR2xXAJQ=/1600x0/https%3A%2F%2Fprod.static9.net.au%2Ffs%2F83fbbb1c-c912-456d-a8ff-1a97def10425", "General news description 3"));

        RelatedNewsAdapter adapter = new RelatedNewsAdapter(relatedNewsList, item -> updateDetailContent(item));
        relatedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        relatedRecyclerView.setAdapter(adapter);
    }
}

