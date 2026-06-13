package com.example.a1212141_courseproject.fragments;

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
import com.example.a1212141_courseproject.adapters.TripsAdapter;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.Trip;

import java.util.List;

public class SpecialFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmptySpecial;
    private TripsAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Trip> specialTrips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_special, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewSpecial);
        tvEmptySpecial = view.findViewById(R.id.tvEmptySpecial);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        specialTrips = databaseHelper.getSpecialTrips();

        if (specialTrips.isEmpty()) {
            tvEmptySpecial.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptySpecial.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter = new TripsAdapter(getContext(), specialTrips, tripId -> {
            Toast.makeText(getContext(), "Trip ID: " + tripId, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (databaseHelper != null && adapter != null) {
            specialTrips = databaseHelper.getSpecialTrips();
            adapter.updateList(specialTrips);

            if (specialTrips.isEmpty()) {
                tvEmptySpecial.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                tvEmptySpecial.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
    }
}