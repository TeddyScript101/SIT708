package com.example.itube;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuFragment extends Fragment {
    EditText youtubeUrlEditText;
    Button playButton, addToPlaylistButton, myPlaylistButton;
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_player, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String currentUsername = sharedPreferences.getString("currentUsername", null);

        dbHelper = new DBHelper(requireContext());

        youtubeUrlEditText = view.findViewById(R.id.youtubeUrlInput);
        playButton = view.findViewById(R.id.playButton);
        addToPlaylistButton = view.findViewById(R.id.addToPlaylistButton);
        myPlaylistButton = view.findViewById(R.id.myPlaylistButton);

        playButton.setOnClickListener(v -> {
            String youtubeUrl = youtubeUrlEditText.getText().toString();
            String videoId = extractVideoId(youtubeUrl);
            if (videoId != null) {
                Bundle bundle = new Bundle();
                bundle.putString("videoId", videoId);

                VideoPlayerFragment videoFragment = new VideoPlayerFragment();
                videoFragment.setArguments(bundle);

                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, videoFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                Toast.makeText(getContext(), "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        addToPlaylistButton.setOnClickListener(v -> {
            String youtubeUrl = youtubeUrlEditText.getText().toString().trim();

            if (youtubeUrl.isEmpty()) {
                Toast.makeText(getContext(), "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
            } else {
                boolean success = dbHelper.addToPlaylist(currentUsername, youtubeUrl);
//                SQLiteDatabase db = dbHelper.getWritableDatabase();
//                db.execSQL("DELETE FROM playlists");
//                db.close();

                if (success) {
                    Toast.makeText(getContext(), "Added to Playlist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Video is already in the Playlist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        myPlaylistButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new PlaylistFragment())
                    .addToBackStack(null)
                    .commit();
        });


        return view;
    }

    private String extractVideoId(String youtubeUrl) {
        String videoId = null;

        Pattern pattern = Pattern.compile("(?:https?://(?:www\\.)?youtube\\.com/(?:watch\\?v=|embed/)|youtu\\.be/)([a-zA-Z0-9_-]+)");
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.find()) {
            videoId = matcher.group(1);
        }
        return videoId;
    }
}
