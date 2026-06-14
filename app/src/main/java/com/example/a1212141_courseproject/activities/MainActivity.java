package com.example.a1212141_courseproject.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.fragments.FavoriteFragment;
import com.example.a1212141_courseproject.fragments.HomeFragment;
import com.example.a1212141_courseproject.fragments.ProfileFragment;
import com.example.a1212141_courseproject.fragments.ReservationsFragment;
import com.example.a1212141_courseproject.fragments.SpecialFragment;
import com.example.a1212141_courseproject.fragments.TripsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            toolbar.setTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    selectedFragment = new HomeFragment();
                    toolbar.setTitle("Home");

                } else if (id == R.id.nav_trips) {
                    selectedFragment = new TripsFragment();
                    toolbar.setTitle("Trips");

                } else if (id == R.id.nav_reservations) {

                    selectedFragment = new ReservationsFragment();
                    toolbar.setTitle("My Reservations");

                } else if (id == R.id.nav_favorites) {
                    selectedFragment = new FavoriteFragment();
                    toolbar.setTitle("Favorites");

                } else if (id == R.id.nav_special) {
                    selectedFragment = new SpecialFragment();
                    toolbar.setTitle("Special Section");

                } else if (id == R.id.nav_logout) {
                    logout();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;

                } else if (id == R.id.nav_reservations) {
                    Toast.makeText(MainActivity.this, "My Reservations - Coming Soon", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_profile) {
                    selectedFragment = new ProfileFragment();
                    toolbar.setTitle("Profile Management");
                } else if (id == R.id.nav_contact) {
                    Intent intent = new Intent(MainActivity.this, ContactUsActivity.class);
                    startActivity(intent);
                }

                if (selectedFragment != null) {
                    loadFragment(selectedFragment);
                    navigationView.setCheckedItem(id);
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @SuppressLint("GestureBackNavigation")
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}