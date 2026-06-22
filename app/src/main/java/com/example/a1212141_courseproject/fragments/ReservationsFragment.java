package com.example.a1212141_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.Reservation;

import java.util.List;

public class ReservationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private ReservationsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservations, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadReservations();
    }

    private void loadReservations() {
        int userId = sharedPreferences.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "الرجاء تسجيل الدخول", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Reservation> reservations = databaseHelper.getReservationsByUserId(userId);

        if (adapter == null) {
            adapter = new ReservationsAdapter(reservations);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(reservations);
        }
    }

    private static class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {

        private List<Reservation> reservations;

        ReservationsAdapter(List<Reservation> reservations) {
            this.reservations = reservations;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_reservation, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Reservation reservation = reservations.get(position);

            holder.tvTripName.setText(reservation.getTripName());
            holder.tvQuantity.setText("👥 العدد: " + reservation.getQuantity());
            holder.tvDate.setText("📅 " + reservation.getReservationDate());
            holder.tvStatus.setText("✅ " + reservation.getStatus());
        }

        @Override
        public int getItemCount() {
            return reservations.size();
        }

        public void updateList(List<Reservation> newReservations) {
            this.reservations = newReservations;
            notifyDataSetChanged();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTripName, tvQuantity, tvDate, tvStatus;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTripName = itemView.findViewById(R.id.tvReservationTripName);
                tvQuantity = itemView.findViewById(R.id.tvReservationQuantity);
                tvDate = itemView.findViewById(R.id.tvReservationDate);
                tvStatus = itemView.findViewById(R.id.tvReservationStatus);
            }
        }
    }
}