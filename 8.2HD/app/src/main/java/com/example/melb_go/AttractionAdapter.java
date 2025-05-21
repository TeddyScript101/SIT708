package com.example.melb_go;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.model.TouristAttraction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
            boolean currentState = attraction.isBookmarked();

            if (!currentState) {

                apiService.bookmarkAttraction(attraction.getId(), "Bearer " + token)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    attraction.setBookmarked(true);
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Bookmarked successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Please Login to enjoy the bookmark feature", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Bookmark API error", Toast.LENGTH_SHORT).show();
                                Log.e("Bookmark", "API error", t);
                            }
                        });
            } else {
                apiService.unbookmarkAttraction(attraction.getId(), "Bearer " + token)
                        .enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    attraction.setBookmarked(false);
                                    notifyItemChanged(position);
                                    Toast.makeText(context, "Removed from bookmarks", Toast.LENGTH_SHORT).show();
                                    Log.d("Bookmark", "Unbookmarked successfully");
                                } else {
                                    Toast.makeText(context, "Failed to unbookmark", Toast.LENGTH_SHORT).show();
                                    Log.e("Bookmark", "Failed to unbookmark: " + response.code());
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(context, "Unbookmark API error", Toast.LENGTH_SHORT).show();
                                Log.e("Bookmark", "API error", t);
                            }
                        });
            }
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
