package com.example.a1212141_courseproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.User;

public class ProfileFragment extends Fragment {

    private EditText etEmail, etFirstName, etLastName, etPhone, etPassword, etConfirmPassword;
    private Button btnUpdateProfile;

    private DatabaseHelper databaseHelper;
    private SharedPreferences sharedPreferences;
    private int userId;
    private User currentUser;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        databaseHelper = new DatabaseHelper(requireContext());
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("user_id", -1);

        etEmail = view.findViewById(R.id.etProfileEmail);
        etFirstName = view.findViewById(R.id.etProfileFirstName);
        etLastName = view.findViewById(R.id.etProfileLastName);
        etPhone = view.findViewById(R.id.etProfilePhone);
        etPassword = view.findViewById(R.id.etProfilePassword);
        etConfirmPassword = view.findViewById(R.id.etProfileConfirmPassword);
        btnUpdateProfile = view.findViewById(R.id.btnUpdateProfile);

        loadUserData();

        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        return view;
    }

    private void loadUserData() {
        if (userId == -1) {
            Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show();
            return;
        }

        currentUser = databaseHelper.getUserById(userId);

        if (currentUser != null) {
            etEmail.setText(currentUser.getEmail());
            etFirstName.setText(currentUser.getFirstName());
            etLastName.setText(currentUser.getLastName());
            etPhone.setText(currentUser.getPhone());
        } else {
            Toast.makeText(requireContext(), "Failed to load profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfile() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || firstName.length() < 3) {
            etFirstName.setError("First name must be at least 3 characters");
            return;
        }

        if (TextUtils.isEmpty(lastName) || lastName.length() < 3) {
            etLastName.setError("Last name must be at least 3 characters");
            return;
        }

        if (!password.isEmpty()) {
            if (password.length() < 6) {
                etPassword.setError("Password must be at least 6 characters");
                return;
            }

            if (!password.matches(".*[A-Za-z].*") || !password.matches(".*[0-9].*")) {
                etPassword.setError("Password must contain at least 1 letter and 1 number");
                return;
            }

            if (!password.equals(confirmPassword)) {
                etConfirmPassword.setError("Passwords do not match");
                return;
            }
        }

        currentUser.setFirstName(firstName);
        currentUser.setLastName(lastName);
        currentUser.setPhone(phone);

        if (!password.isEmpty()) {
            currentUser.setPassword(password);
        } else {
            currentUser.setPassword("");
        }

        boolean updated = databaseHelper.updateUser(currentUser);

        if (updated) {
            Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
            etPassword.setText("");
            etConfirmPassword.setText("");
        } else {
            Toast.makeText(requireContext(), "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
}