package com.example.a1212141_courseproject.activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.User;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etFirstName, etLastName, etPassword, etConfirmPassword, etPhone;
    private Spinner spinnerGender, spinnerMajor;
    private Button btnRegister;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Database
        databaseHelper = new DatabaseHelper(this);

        etEmail = findViewById(R.id.etEmail);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        spinnerGender = findViewById(R.id.spinnerGender);
        spinnerMajor = findViewById(R.id.spinnerMajor);
        btnRegister = findViewById(R.id.btnRegister);

        // Gender Spinner
        String[] genders = {"Select Gender", "Male", "Female"};
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, genders);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(genderAdapter);

        // Major Spinner
        String[] majors = {"Select Major", "Computer Science", "Engineering", "Business",
                "Medicine", "Arts", "Sciences", "Other"};
        ArrayAdapter<String> majorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, majors);
        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMajor.setAdapter(majorAdapter);

        btnRegister.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = etEmail.getText().toString().trim();
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String major = spinnerMajor.getSelectedItem().toString();

        // Validate Email
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }
        if (!Pattern.compile(String.valueOf(android.util.Patterns.EMAIL_ADDRESS)).matcher(email).matches()) {
            etEmail.setError("Enter valid email");
            return;
        }

        // Validate First Name (min 3 characters)
        if (TextUtils.isEmpty(firstName) || firstName.length() < 3) {
            etFirstName.setError("First name must be at least 3 characters");
            return;
        }

        // Validate Last Name (min 3 characters)
        if (TextUtils.isEmpty(lastName) || lastName.length() < 3) {
            etLastName.setError("Last name must be at least 3 characters");
            return;
        }

        // Validate Password (min 6 chars, at least 1 letter and 1 number)
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Password is required");
            return;
        }
        if (password.length() < 6) {
            etPassword.setError("Password must be at least 6 characters");
            return;
        }
        if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
            etPassword.setError("Password must contain at least 1 letter and 1 number");
            return;
        }

        // Validate Confirm Password
        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("Passwords do not match");
            return;
        }

        //Validate Gender
        if (gender.equals("Select Gender")) {
            Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
            return;
        }

        //Validate Major
        if (major.equals("Select Major")) {
            Toast.makeText(this, "Please select major", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if email already exists
        if (databaseHelper.isEmailExists(email)) {
            Toast.makeText(this, "Email already registered!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User
        User newUser = new User(email, firstName, lastName, password, gender, phone);

        // Save to database
        if (databaseHelper.addUser(newUser)) {
            Toast.makeText(this, "Registration successful! Please login.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}