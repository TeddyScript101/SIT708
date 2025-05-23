package com.example.melb_go.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.melb_go.R;
import com.example.melb_go.model.TouristAttraction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectAttractionsAdapter extends RecyclerView.Adapter<SelectAttractionsAdapter.ViewHolder> {

    private List<TouristAttraction> attractions;
    private Set<TouristAttraction> selectedItems = new HashSet<>();

    public SelectAttractionsAdapter(List<TouristAttraction> attractions) {
        this.attractions = attractions;
    }

    public Set<TouristAttraction> getSelectedItems() {
        return selectedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_attraction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TouristAttraction attraction = attractions.get(position);
        holder.tvName.setText(attraction.getFeatureName());

        Glide.with(holder.imgAttraction.getContext())
                .load(attraction.getImageUrl())
                .into(holder.imgAttraction);


        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(selectedItems.contains(attraction));

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                selectedItems.add(attraction);
            } else {
                selectedItems.remove(attraction);
            }
        });
    }

    @Override
    public int getItemCount() {
        return attractions.size();
    }
    public void setData(List<TouristAttraction> newData) {
        this.attractions.clear();
        this.attractions.addAll(newData);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        CheckBox checkBox;
        ImageView imgAttraction;
        ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tvAttractionName);
            checkBox = view.findViewById(R.id.checkboxSelect);
            imgAttraction = view.findViewById(R.id.imgAttraction);
        }
    }
}
