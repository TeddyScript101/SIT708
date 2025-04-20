package com.example.itube;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistFragment extends Fragment {
    private RecyclerView recyclerView;
    private PlaylistAdapter adapter;
    private DBHelper dbHelper;

    public PlaylistFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);

        recyclerView = view.findViewById(R.id.playlistRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Button backButton = view.findViewById(R.id.backButton);
        dbHelper = new DBHelper(getContext());

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("currentUsername", null);

        List<String> playlist = dbHelper.getPlaylist(currentUsername);

        adapter = new PlaylistAdapter(playlist);
        recyclerView.setAdapter(adapter);

        backButton.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .popBackStack());

        return view;
    }
}
