package com.example.a1212141_courseproject.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.Reservation;
import com.example.a1212141_courseproject.models.Trip;
import com.example.a1212141_courseproject.models.User;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;
    private TextView tvHeaderName, tvHeaderEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(this);

        setupToolbar();
        setupDrawer();
        updateNavHeader();

        if (savedInstanceState == null) {
            showDashboard();
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupDrawer() {
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open, R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void updateNavHeader() {
        View headerView = navigationView.getHeaderView(0);
        tvHeaderName = headerView.findViewById(R.id.navHeaderName);
        tvHeaderEmail = headerView.findViewById(R.id.navHeaderEmail);

        String email = sharedPreferences.getString("user_email", "admin@admin.com");
        tvHeaderName.setText("Manager");
        tvHeaderEmail.setText(email);
    }

    private void showDashboard() {
        toolbar.setTitle("Admin Home");

        View dashboardView = getLayoutInflater().inflate(R.layout.admin_dashboard, null);
        setContentView(dashboardView);

        //  Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        setupToolbar();
        setupDrawer();
        updateNavHeader();

        // display information in the dashboard of user and trip
        TextView tvStats = findViewById(R.id.tvStats);
        int usersCount = databaseHelper.getAllUsers().size();
        int tripsCount = databaseHelper.getAllTrips().size();
        int reservationsCount = databaseHelper.getAllReservations().size();

        tvStats.setText("Users: " + usersCount + "\n" +
                "Trips: " + tripsCount + "\n" +
                "Reservations: " + reservationsCount);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            showDashboard();
        } else if (id == R.id.nav_view_users) {
            showUsersDialog();
        } else if (id == R.id.nav_add_admin) {
            showAddAdminDialog();
        } else if (id == R.id.nav_view_trips) {
            showTripsDialog();
        } else if (id == R.id.nav_add_trip) {
            showAddTripDialog();
        } else if (id == R.id.nav_delete_trip) {
            showDeleteTripDialog();
        } else if (id == R.id.nav_edit_trip) {
            showEditTripDialog();
        }else if (id == R.id.nav_view_reservations) {
            showReservationsDialog();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    //Display User
    private void showUsersDialog() {
        List<User> users = databaseHelper.getAllUsers();

        if (users.isEmpty()) {
            Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] userNames = new String[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            userNames[i] = user.getEmail() + " - " + user.getFirstName() + " " + user.getLastName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List of users");
        builder.setItems(userNames, (dialog, which) -> {
            User selectedUser = users.get(which);
            showDeleteUserConfirmDialog(selectedUser);
        });
        builder.setNegativeButton("Cancle", null);
        builder.show();
    }

    // Delete user
    private void showDeleteUserConfirmDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure you want to delete user: " + user.getEmail() + "؟");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            if (databaseHelper.deleteUser(user.getId())) {
                Toast.makeText(this, "deleted user", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    //Display Tri
    private void showTripsDialog() {
        List<Trip> trips = databaseHelper.getAllTrips();

        if (trips.isEmpty()) {
            Toast.makeText(this, "No trips found", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] tripNames = new String[trips.size()];
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            tripNames[i] = trip.getDestination() + " - " + trip.getCountry() + " (" + trip.getPrice() + "$)";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List of trips");
        builder.setItems(tripNames, null);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    //Add Admin
    private void showAddAdminDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_admin, null);
        builder.setView(dialogView);

        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etFirstName = dialogView.findViewById(R.id.etFirstName);
        EditText etLastName = dialogView.findViewById(R.id.etLastName);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            User newAdmin = new User(email, firstName, lastName, password, "Admin", "0000000000");

            if (databaseHelper.addUser(newAdmin)) {
                Toast.makeText(this, "Admin added successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add admin", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    // Edit Trip
    private void showEditTripDialog() {
        List<Trip> trips = databaseHelper.getAllTrips();

        if (trips.isEmpty()) {
            Toast.makeText(this, "No trips found", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] tripNames = new String[trips.size()];
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            tripNames[i] = trip.getDestination() + " - " + trip.getCountry() + " (" + trip.getPrice() + "$)";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a trip to edit");
        builder.setItems(tripNames, (dialog, which) -> {
            Trip selectedTrip = trips.get(which);
            showEditTripForm(selectedTrip);
        });
        builder.setNegativeButton("Cancle", null);
        builder.show();
    }

    private void showEditTripForm(Trip trip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_trip, null);
        builder.setView(dialogView);

        EditText etDestination = dialogView.findViewById(R.id.etDestination);
        EditText etCountry = dialogView.findViewById(R.id.etCountry);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etDuration = dialogView.findViewById(R.id.etDuration);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        EditText etRating = dialogView.findViewById(R.id.etRating);
        EditText etImageUrl = dialogView.findViewById(R.id.etImageUrl);
        Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        etDestination.setText(trip.getDestination());
        etCountry.setText(trip.getCountry());
        etDescription.setText(trip.getDescription());
        etDuration.setText(String.valueOf(trip.getDurationDays()));
        etPrice.setText(String.valueOf(trip.getPrice()));
        etRating.setText(String.valueOf(trip.getRating()));
        etImageUrl.setText(trip.getImageUrl());

        AlertDialog dialog = builder.create();

        btnUpdate.setOnClickListener(v -> {
            String destination = etDestination.getText().toString().trim();
            String country = etCountry.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String durationStr = etDuration.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String ratingStr = etRating.getText().toString().trim();
            String imageUrl = etImageUrl.getText().toString().trim();

            if (destination.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Destination and Country are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = durationStr.isEmpty() ? 3 : Integer.parseInt(durationStr);
            double price = priceStr.isEmpty() ? 500 : Double.parseDouble(priceStr);
            double rating = ratingStr.isEmpty() ? 4.0 : Double.parseDouble(ratingStr);

            // edit trip
            trip.setDestination(destination);
            trip.setCountry(country);
            trip.setDescription(description);
            trip.setDurationDays(duration);
            trip.setPrice(price);
            trip.setRating(rating);
            trip.setImageUrl(imageUrl);

            if (databaseHelper.updateTrip(trip)) {
                Toast.makeText(this, "Updated trip successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                showDashboard();
            } else {
                Toast.makeText(this, "Failed to update trip", Toast.LENGTH_SHORT).show();
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    // Add Trip
    private void showAddTripDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_trip, null);
        builder.setView(dialogView);

        EditText etDestination = dialogView.findViewById(R.id.etDestination);
        EditText etCountry = dialogView.findViewById(R.id.etCountry);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etDuration = dialogView.findViewById(R.id.etDuration);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);
        EditText etRating = dialogView.findViewById(R.id.etRating);
        EditText etImageUrl = dialogView.findViewById(R.id.etImageUrl);
        Button btnSave = dialogView.findViewById(R.id.btnSave);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String destination = etDestination.getText().toString().trim();
            String country = etCountry.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String durationStr = etDuration.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String ratingStr = etRating.getText().toString().trim();
            String imageUrl = etImageUrl.getText().toString().trim();

            if (destination.isEmpty() || country.isEmpty()) {
                Toast.makeText(this, "Destination and Country are required", Toast.LENGTH_SHORT).show();
                return;
            }

            int duration = durationStr.isEmpty() ? 3 : Integer.parseInt(durationStr);
            double price = priceStr.isEmpty() ? 500 : Double.parseDouble(priceStr);
            double rating = ratingStr.isEmpty() ? 4.0 : Double.parseDouble(ratingStr);

            Trip newTrip = new Trip(0, destination, country, description, duration,
                    "2024-12-01", price, rating, imageUrl);

            if (databaseHelper.insertTrip(newTrip)) {
                Toast.makeText(this, "Added trip successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to add trip", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    // Delete Trip
    private void showDeleteTripDialog() {
        List<Trip> trips = databaseHelper.getAllTrips();

        if (trips.isEmpty()) {
            Toast.makeText(this, "No trips found", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] tripNames = new String[trips.size()];
        for (int i = 0; i < trips.size(); i++) {
            Trip trip = trips.get(i);
            tripNames[i] = trip.getDestination() + " - " + trip.getCountry() + " (" + trip.getPrice() + "$)";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a trip to delete");
        builder.setItems(tripNames, (dialog, which) -> {
            Trip selectedTrip = trips.get(which);
            confirmDeleteTrip(selectedTrip);
        });
        builder.setNegativeButton("Cancle", null);
        builder.show();
    }

    private void confirmDeleteTrip(Trip trip) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Trip");
        builder.setMessage("Are you sure you want to delete trip: " + trip.getDestination() + "؟");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            databaseHelper.deleteTrip(trip.getId());
            Toast.makeText(this, "Deleted trip successfully", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancle", null);
        builder.show();
    }

    //Display Reservation
    private void showReservationsDialog() {
        List<Reservation> reservations = databaseHelper.getAllReservations();

        if (reservations.isEmpty()) {
            Toast.makeText(this, "No reservations found", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] reservationDetails = new String[reservations.size()];
        for (int i = 0; i < reservations.size(); i++) {
            Reservation res = reservations.get(i);
            Trip trip = databaseHelper.getTripById(res.getTripId());
            String tripName = (trip != null) ? trip.getDestination() : "Unknown";
            reservationDetails[i] = tripName + "Quantity -: " + res.getQuantity() + "Date -: " + res.getReservationDate();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("List of reservations");
        builder.setItems(reservationDetails, null);
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Logout
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}