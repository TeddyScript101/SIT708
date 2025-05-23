package com.example.melb_go.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.melb_go.R;
import com.example.melb_go.model.OptimizedRouteDay;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {

    private List<OptimizedRouteDay> dayRoutes;

    public RouteAdapter(List<OptimizedRouteDay> dayRoutes) {
        this.dayRoutes = dayRoutes;
    }

    @NonNull
    @Override
    public RouteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_route, parent, false);
        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteViewHolder holder, int position) {
        OptimizedRouteDay dayRoute = dayRoutes.get(position);
        holder.dayTextView.setText("Day " + dayRoute.day);
        holder.summaryTextView.setText(dayRoute.summary);

        StringBuilder details = new StringBuilder();
        for (OptimizedRouteDay.Place place : dayRoute.route) {
            details.append(place.order).append(". ").append(place.name)
                    .append("\n  Travel: ").append(place.travel_time_from_previous_min).append(" min")
                    .append(", Stay: ").append(place.time_to_stay_min).append(" min\n");
        }
        holder.detailsTextView.setText(details.toString());

        holder.totalsTextView.setText(
                "Total Travel Time: " + formatTime(dayRoute.total_travel_time_min) + "\n" +
                        "Total Stay Time: " + formatTime(dayRoute.total_stay_time_min) + "\n" +
                        "Total Time: " + formatTime(dayRoute.total_time_min)
        );
    }

    @Override
    public int getItemCount() {
        return dayRoutes.size();
    }

    static class RouteViewHolder extends RecyclerView.ViewHolder {
        TextView dayTextView, summaryTextView, detailsTextView, totalsTextView;

        public RouteViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.dayTextView);
            summaryTextView = itemView.findViewById(R.id.summaryTextView);
            detailsTextView = itemView.findViewById(R.id.detailsTextView);
            totalsTextView = itemView.findViewById(R.id.totalsTextView);
        }
    }
    private String formatTime(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        } else {
            return minutes + "m";
        }
    }
}


