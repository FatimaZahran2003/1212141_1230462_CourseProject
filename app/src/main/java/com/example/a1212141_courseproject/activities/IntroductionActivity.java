package com.example.a1212141_courseproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.api.RetrofitClient;
import com.example.a1212141_courseproject.api.ApiService;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroductionActivity extends AppCompatActivity {

    private Button btnConnect;
    private ProgressBar progressBar;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        btnConnect = findViewById(R.id.btnConnect);
        progressBar = findViewById(R.id.progressBar);
        databaseHelper = new DatabaseHelper(this);

        if (btnConnect == null) {
            Toast.makeText(this, "Error! btnConnect is null", Toast.LENGTH_LONG).show();
            return;
        }

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IntroductionActivity.this, "Connect!", Toast.LENGTH_SHORT).show();
                connectToAPI();
            }
        });
    }

    private void connectToAPI() {
        //  ProgressBar
        progressBar.setVisibility(View.VISIBLE);
        btnConnect.setEnabled(false);
        btnConnect.setText(" Connecting...");

        // connect to  API
        ApiService apiService = RetrofitClient.getApiService();
        Call<List<Trip>> call = apiService.getTrips();

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                progressBar.setVisibility(View.GONE);
                btnConnect.setEnabled(true);
                btnConnect.setText("Connect");

                if (response.isSuccessful() && response.body() != null) {
                    List<Trip> trips = response.body();

                    databaseHelper.deleteAllTrips();
                    int savedCount = 0;
                    for (Trip trip : trips) {
                        if (databaseHelper.insertTrip(trip)) {
                            savedCount++;
                        }
                    }

                    Toast.makeText(IntroductionActivity.this,
                            "Loading trips save..." + savedCount  + trips.size() ,
                            Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(IntroductionActivity.this,
                            " Error loading...: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnConnect.setEnabled(true);
                btnConnect.setText("Connect");

                Toast.makeText(IntroductionActivity.this,
                        " Error connect..: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}