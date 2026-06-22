package com.example.a1212141_courseproject.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private TripsAdapter adapter;
    private DatabaseHelper databaseHelper;
    private List<Trip> favoriteTrips;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseHelper = new DatabaseHelper(getContext());
        favoriteTrips = databaseHelper.getFavoriteTrips();

        adapter = new TripsAdapter(getContext(), favoriteTrips, tripId -> {
            Toast.makeText(getContext(), "Trip ID: " + tripId, Toast.LENGTH_SHORT).show();
            }, true);


        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (databaseHelper != null && adapter != null) {
            favoriteTrips = databaseHelper.getFavoriteTrips();
            adapter.updateList(favoriteTrips);
        }
    }
}