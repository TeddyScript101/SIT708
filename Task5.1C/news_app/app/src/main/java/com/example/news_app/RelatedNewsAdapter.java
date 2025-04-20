package com.example.news_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.ViewHolder> {

    private List<NewsItem> relatedList;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    public RelatedNewsAdapter(List<NewsItem> relatedList, OnItemClickListener listener) {
        this.relatedList = relatedList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_vertical, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsItem item = relatedList.get(position);
        holder.title.setText(item.getTitle());
        Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).into(holder.image);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return relatedList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.text_title);
            image = itemView.findViewById(R.id.image_news);
        }
        public void bind(final NewsItem item, final OnItemClickListener listener) {
            title.setText(item.getTitle());
            Glide.with(itemView.getContext()).load(item.getImageUrl()).into(image);
            itemView.setOnClickListener(v -> listener.onItemClick(item));
        }
    }
}
