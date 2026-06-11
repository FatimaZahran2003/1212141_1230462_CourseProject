package com.example.a1212141_courseproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.Trip;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private Context context;
    private List<Trip> trips;
    private DatabaseHelper databaseHelper;
    private OnTripClickListener listener;

    // Interface للتعامل مع الضغط على الرحلة
    public interface OnTripClickListener {
        void onTripClick(int tripId);
    }

    // Constructor مع 3 parameters
    public TripsAdapter(Context context, List<Trip> trips, OnTripClickListener listener) {
        this.context = context;
        this.trips = trips;
        this.listener = listener;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.tvDestination.setText(trip.getDestination());
        holder.tvCountry.setText(trip.getCountry());
        holder.tvDuration.setText(trip.getDurationDays() + " days");
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvRating.setText("⭐ " + trip.getRating());
        holder.tvDescription.setText(trip.getDescription());

        // Favorite button
        if (trip.isFavorite()) {
            holder.btnFavorite.setText("❤️");
        } else {
            holder.btnFavorite.setText("♡");
        }

        holder.btnFavorite.setOnClickListener(v -> {
            if (trip.isFavorite()) {
                databaseHelper.removeFromFavorites(trip.getId());
                trip.setFavorite(false);
                holder.btnFavorite.setText("♡");
                Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show();
            } else {
                databaseHelper.addToFavorites(trip.getId());
                trip.setFavorite(true);
                holder.btnFavorite.setText("❤️");
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        // Reserve button
        holder.btnReserve.setOnClickListener(v -> {
            Toast.makeText(context, "Reserve " + trip.getDestination(), Toast.LENGTH_SHORT).show();
        });

        // Item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onTripClick(trip.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void updateList(List<Trip> newList) {
        this.trips = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDestination, tvCountry, tvDuration, tvPrice, tvRating, tvDescription;
        Button btnFavorite, btnReserve;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDestination = itemView.findViewById(R.id.tvDestination);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            btnFavorite = itemView.findViewById(R.id.btnFavorite);
            btnReserve = itemView.findViewById(R.id.btnReserve);
        }
    }
}