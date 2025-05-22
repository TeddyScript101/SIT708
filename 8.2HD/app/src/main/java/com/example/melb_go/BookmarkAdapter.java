package com.example.melb_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melb_go.api.ApiService;
import com.example.melb_go.model.TouristAttraction;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    private List<TouristAttraction> data;
    private final ApiService apiService;
    private final String token;
    private final Context context;
    private final OnAttractionClickListener clickListener;

    public interface OnAttractionClickListener {
        void onClick(String attractionId);
    }

    public BookmarkAdapter(Context context, List<TouristAttraction> attractions, ApiService apiService, String token, OnAttractionClickListener clickListener) {
        this.context = context;
        this.data = attractions; // ✅ Corrected: assign attractions
        this.apiService = apiService;
        this.token = token;
        this.clickListener = clickListener; // ✅ Assign the listener
    }

    public void updateData(List<TouristAttraction> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_attraction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.ViewHolder holder, int position) {
        TouristAttraction attraction = data.get(position);
        holder.featureName.setText(attraction.getFeatureName());
        holder.themeText.setText(attraction.getTheme() + " - " + attraction.getSubTheme());

        Glide.with(holder.itemView.getContext())
                .load(attraction.getImageUrl())
                .into(holder.imageView);

        holder.bookmarkIcon.setImageResource(
                attraction.isBookmarked() ? R.drawable.filledheart : R.drawable.heart
        );

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onClick(attraction.getId()); // 🔁 Use correct ID field
            }
        });

        holder.bookmarkIcon.setOnClickListener(v -> {
            BookmarkManager.toggleBookmark(
                    context,
                    attraction,
                    apiService,
                    token,
                    true,
                    () -> {
                        notifyItemChanged(holder.getAdapterPosition());
                        Toast.makeText(context,
                                attraction.isBookmarked() ? "Bookmarked" : "Unbookmarked",
                                Toast.LENGTH_SHORT).show();
                    },
                    () -> Toast.makeText(context, "Failed to update bookmark", Toast.LENGTH_SHORT).show()
            );
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView, bookmarkIcon;
        TextView featureName, themeText;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            bookmarkIcon = view.findViewById(R.id.bookmarkIcon);
            featureName = view.findViewById(R.id.featureName);
            themeText = view.findViewById(R.id.themeText);
        }
    }
}
