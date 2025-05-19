package com.example.melb_go;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AttractionAdapter extends RecyclerView.Adapter<AttractionAdapter.ViewHolder> {

    private Context context;
    private List<TouristAttraction> attractionList;

    public AttractionAdapter(Context context, List<TouristAttraction> list) {
        this.context = context;
        this.attractionList = list;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView featureName, themeText;

        public ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageView);
            featureName = view.findViewById(R.id.featureName);
            themeText = view.findViewById(R.id.themeText);
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
