package com.example.a1212141_courseproject.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.List;

public class TripsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripsAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Trip> tripList;
    private List<Trip> filteredList;
    private SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTrips);
        searchView = view.findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        tripList = databaseHelper.getAllTrips();
        filteredList = new ArrayList<>(tripList);

        adapter = new TripsAdapter(getContext(), filteredList, tripId -> {

            Toast.makeText(getContext(), "Trip ID: " + tripId, Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        // Search
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterTrips(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterTrips(newText);
                return true;
            }
        });

        return view;
    }

    private void filterTrips(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(tripList);
        } else {
            for (Trip trip : tripList) {
                if (trip.getDestination().toLowerCase().contains(query.toLowerCase()) ||
                        trip.getCountry().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(trip);
                }
            }
        }
        adapter.updateList(filteredList);
    }
}