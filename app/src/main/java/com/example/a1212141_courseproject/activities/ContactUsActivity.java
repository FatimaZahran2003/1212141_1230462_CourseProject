package com.example.a1212141_courseproject.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.a1212141_courseproject.R;

public class ContactUsActivity extends AppCompatActivity {

    private Button btnCall, btnLocate, btnEmail;

    // Data
    private static final String PHONE_NUMBER = "+970123456789";
    private static final String LOCATION_LAT = "31.9474";
    private static final String LOCATION_LNG = "35.2272";
    private static final String LOCATION_NAME = "Birzeit University";
    private static final String EMAIL_ADDRESS = "travel@planner.com";
    private static final String EMAIL_SUBJECT = "Any Questions for Travel Planner";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        btnCall = findViewById(R.id.btnCall);
        btnLocate = findViewById(R.id.btnLocate);
        btnEmail = findViewById(R.id.btnEmail);

        btnCall.setOnClickListener(v -> makePhoneCall());
        btnLocate.setOnClickListener(v -> openMaps());
        btnEmail.setOnClickListener(v -> sendEmail());
    }

    private void makePhoneCall() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + PHONE_NUMBER));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No phone app", Toast.LENGTH_SHORT).show();
        }
    }

    private void openMaps() {
        try {
            //  Google Maps
            Uri uri = Uri.parse("geo:" + LOCATION_LAT + "," + LOCATION_LNG + "?q=" + Uri.encode(LOCATION_NAME));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage("com.google.android.apps.maps");

            if (intent.resolveActivity(getPackageManager()) == null) {
                intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.google.com/maps/place/Birzeit+University/@31.9474,35.2272,17" + LOCATION_LAT + "," + LOCATION_LNG));
            }

            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No maps app", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendEmail() {
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + EMAIL_ADDRESS));
            intent.putExtra(Intent.EXTRA_SUBJECT, EMAIL_SUBJECT);
            intent.putExtra(Intent.EXTRA_TEXT, "\n\n\n---\nSend from app Travel Planner");

            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "No email app", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}