package com.example.melb_go.adapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melb_go.R;
import com.example.melb_go.model.TouristAttraction;

import java.util.List;

public class ScheduleAttractionsAdapter extends RecyclerView.Adapter<ScheduleAttractionsAdapter.ViewHolder> {

    private List<TouristAttractionWithTime> attractionList;

    public ScheduleAttractionsAdapter(List<TouristAttractionWithTime> attractionList) {
        this.attractionList = attractionList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        EditText etTimeHours;
        EditText etTimeMinutes;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvAttractionName);
            etTimeHours = itemView.findViewById(R.id.etTimeHours);
            etTimeMinutes = itemView.findViewById(R.id.etTimeMinutes);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }


    @NonNull
    @Override
    public ScheduleAttractionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_schedule_attraction, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleAttractionsAdapter.ViewHolder holder, int position) {
        TouristAttractionWithTime item = attractionList.get(position);
        holder.tvName.setText(item.attraction.getFeatureName());

        // Remove previous watchers to avoid duplicate triggers (important)
        if (holder.etTimeHours.getTag() instanceof TextWatcher) {
            holder.etTimeHours.removeTextChangedListener((TextWatcher) holder.etTimeHours.getTag());
        }
        if (holder.etTimeMinutes.getTag() instanceof TextWatcher) {
            holder.etTimeMinutes.removeTextChangedListener((TextWatcher) holder.etTimeMinutes.getTag());
        }

        // Set initial values
        holder.etTimeHours.setText(String.valueOf(item.timeHours));
        holder.etTimeMinutes.setText(String.valueOf(item.timeMinutes));

        // Add TextWatcher for hours
        TextWatcher hoursWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString();
                int hours = 0;
                try {
                    hours = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    // ignore invalid input
                }
                item.timeHours = hours;
            }
        };
        holder.etTimeHours.addTextChangedListener(hoursWatcher);
        holder.etTimeHours.setTag(hoursWatcher);

        // Add TextWatcher for minutes
        TextWatcher minutesWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString();
                int mins = 0;
                try {
                    mins = Integer.parseInt(val);
                } catch (NumberFormatException e) {
                    // ignore invalid input
                }
                item.timeMinutes = mins;
            }
        };
        holder.etTimeMinutes.addTextChangedListener(minutesWatcher);
        holder.etTimeMinutes.setTag(minutesWatcher);

        holder.btnDelete.setOnClickListener(v -> {
            attractionList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, attractionList.size());
        });
    }
    @Override
    public int getItemCount() {
        return attractionList.size();
    }

    public void setData(List<TouristAttractionWithTime> newList) {
        attractionList = newList;
        notifyDataSetChanged();
    }

    public List<TouristAttractionWithTime> getData() {
        return attractionList;
    }

    public static class TouristAttractionWithTime {
        public TouristAttraction attraction;
        public int timeHours;
        public int timeMinutes;

        public TouristAttractionWithTime(TouristAttraction attraction) {
            this.attraction = attraction;
            this.timeHours = 0;
            this.timeMinutes = 0;
        }
    }
}
