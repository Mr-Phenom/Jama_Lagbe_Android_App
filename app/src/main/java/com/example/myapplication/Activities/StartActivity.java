package com.example.myapplication.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        Log.d("Login Check", "isLoggedIn: " + isLoggedIn);

        if (isLoggedIn) {
            // User is already logged in, navigate to dashboard
            String email = sharedPreferences.getString("email","");
            Intent intent = new Intent(StartActivity.this, DashboardActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
            finish(); // Close the current activity
        } else {
            // User is not logged in, navigate to login screen
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}