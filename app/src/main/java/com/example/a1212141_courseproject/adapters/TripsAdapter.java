package com.example.a1212141_courseproject.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private Context context;
    private List<Trip> trips;
    private DatabaseHelper databaseHelper;
    private OnTripClickListener listener;

    public interface OnTripClickListener {
        void onTripClick(int tripId);
    }

    public TripsAdapter(Context context, List<Trip> trips, OnTripClickListener listener) {
        this.context = context;
        this.trips = trips;
        this.listener = listener;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public TripsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_trip, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsAdapter.ViewHolder holder, int position) {
        Trip trip = trips.get(position);

        holder.tvDestination.setText(trip.getDestination());
        holder.tvCountry.setText(trip.getCountry());
        holder.tvDuration.setText(trip.getDurationDays() + " days");
        holder.tvPrice.setText("$" + trip.getPrice());
        holder.tvRating.setText("⭐ " + trip.getRating());
        holder.tvDescription.setText(trip.getDescription());

        trip.setFavorite(databaseHelper.isFavorite(trip.getId()));

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

                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    trips.remove(currentPosition);
                    notifyItemRemoved(currentPosition);
                }

            } else {
                databaseHelper.addToFavorites(trip.getId());
                trip.setFavorite(true);
                holder.btnFavorite.setText("❤️");
                Toast.makeText(context, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnReserve.setOnClickListener(v -> {
            SharedPreferences sharedPreferences =
                    context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

            int userId = sharedPreferences.getInt("user_id", -1);

            if (userId == -1) {
                Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();
                return;
            }

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            boolean reserved = databaseHelper.addReservation(
                    userId,
                    trip.getId(),
                    1,
                    date,
                    "Confirmed"
            );

            if (reserved) {
                Toast.makeText(context, "Reservation added successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Reservation failed", Toast.LENGTH_SHORT).show();
            }
        });

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