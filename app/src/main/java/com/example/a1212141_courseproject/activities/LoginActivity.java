package com.example.a1212141_courseproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a1212141_courseproject.R;
import com.example.a1212141_courseproject.database.DatabaseHelper;
import com.example.a1212141_courseproject.models.User;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;
    private CheckBox cbRememberMe;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);

         // if user is login go to Main activity
        if (sharedPreferences.getBoolean("is_logged_in", false)) {
            String email = sharedPreferences.getString("user_email", "");
            if (email.equals("admin@admin.com")) {
                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
            finish();
            return;
        }

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvSignUp);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        // Load saved email
        String savedEmail = sharedPreferences.getString("email", "");
        if (!savedEmail.isEmpty()) {
            etEmail.setText(savedEmail);
            cbRememberMe.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> login());
        tvSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty()) {
            etEmail.setError("Email required");
            return;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password required");
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (cbRememberMe.isChecked()) {
            editor.putString("email", email);
        } else {
            editor.remove("email");
        }
        editor.apply();

        // Check Admin
        if (email.equals("admin@admin.com") && password.equals("Admin123!")) {
            editor.putBoolean("is_logged_in", true);
            editor.putString("user_email", email);
            editor.putInt("user_id", 0);
            editor.apply();

            Toast.makeText(this, "Admin Login Success", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            finish();
        }
        // Check Regular User
        else if (databaseHelper.checkUser(email, password)) {
            User user = databaseHelper.getUserByEmail(email);
            if (user != null) {
                editor.putBoolean("is_logged_in", true);
                editor.putInt("user_id", user.getId());
                editor.putString("user_email", user.getEmail());
                editor.apply();

                Toast.makeText(this, "Welcome " + user.getFirstName() + "!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
        }
    }
}