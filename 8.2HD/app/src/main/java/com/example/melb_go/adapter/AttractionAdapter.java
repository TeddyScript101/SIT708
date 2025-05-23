package com.example.melb_go.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melb_go.BookmarkManager;
import com.example.melb_go.R;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.model.TouristAttraction;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private Context context;
    private List<TouristAttraction> attractionList;
    private OnAttractionClickListener listener;

    private ApiService apiService;
    private String token;

    public interface OnAttractionClickListener {
        void onAttractionClick(String attractionId);
    }

    public AttractionAdapter(Context context, List<TouristAttraction> list, OnAttractionClickListener listener, ApiService apiService, String token) {
        this.context = context;
        this.attractionList = list;
        this.listener = listener;
        this.apiService = apiService;
        this.token = token;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,bookmarkIcon ;
        TextView featureName, themeText;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            featureName = view.findViewById(R.id.featureName);
            themeText = view.findViewById(R.id.themeText);
            bookmarkIcon = view.findViewById(R.id.bookmarkIcon);
        }
    }

    public void clearAttractions() {
        attractionList.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_attraction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TouristAttraction attraction = attractionList.get(position);
        holder.featureName.setText(attraction.getFeatureName());
        holder.themeText.setText(attraction.getTheme() + " - " + attraction.getSubTheme());
        Glide.with(context).load(attraction.getImageUrl()).into(holder.imageView);

        holder.bookmarkIcon.setImageResource(
                attraction.isBookmarked() ? R.drawable.filledheart : R.drawable.heart
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAttractionClick(attraction.getId());
            }
        });

        holder.bookmarkIcon.setOnClickListener(v -> {
            BookmarkManager.toggleBookmark(
                    holder.itemView.getContext(),
                    attraction,
                    apiService,
                    token,
                    true,
                    () -> {
                        holder.bookmarkIcon.setImageResource(
                                attraction.isBookmarked() ? R.drawable.filledheart : R.drawable.heart
                        );
                    },
                    () -> {
                    }
            );
        });



    }

    public void addAttractions(List<TouristAttraction> newAttractions) {
        int startPos = attractionList.size();
        attractionList.addAll(newAttractions);
        notifyItemRangeInserted(startPos, newAttractions.size());
    }

    @Override
    public int getItemCount() {
        return attractionList.size();
    }
}
