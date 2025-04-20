package com.example.itube;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private List<String> playlist;

    public PlaylistAdapter(List<String> playlist) {
        this.playlist = playlist;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView urlText;

        public ViewHolder(View itemView) {
            super(itemView);
            urlText = itemView.findViewById(R.id.urlText);
        }
    }

    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = playlist.get(position);
        holder.urlText.setText(url);
    }

    @Override
    public int getItemCount() {
        return playlist.size();
    }
}
