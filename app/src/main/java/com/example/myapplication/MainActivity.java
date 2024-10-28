package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Activities.DashboardActivity;
import com.example.myapplication.Activities.SignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //public static String ip_address = "10.0.2.2";
    public static String ip_address = "10.0.2.2";


    //private String URL = "http://10.0.2.2/jama_lagbe/login.php";
    private String URL = "http://"+ ip_address +"/app_login.php";
    private EditText userName, password;
    private Button logIn;
    private TextView signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userName = findViewById(R.id.editTextUserNameLogin);
        password = findViewById(R.id.editTextPasswordLogin);

        signUp = findViewById(R.id.textViewNewAccountLogin);
        logIn = findViewById(R.id.buttonLogin);



        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginUser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    //handle login functionality
    private void loginUser() {
        final String USERNAME = userName.getText().toString().trim();
        final String PASSWORD = password.getText().toString().trim();

        //check if fields are empty
        if (USERNAME.isEmpty() || PASSWORD.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Response", "Server Response: " + response); // Log the raw response
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("success")) {
                        // Store login info in SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("email", USERNAME);
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(i);
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse != null) {
                            String errorMsg = new String(error.networkResponse.data);
                            Log.d("Error Response", "Error code: " + error.networkResponse.statusCode);
                            Log.d("Error Response", "Error message: " + errorMsg);
                        }
                        Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("error of sql",error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send username and password to the server via POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("username", USERNAME); // 'username' is the key sent to PHP
                params.put("password", PASSWORD); // 'password' is the key sent to PHP
                return params;
            }
        };

            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(stringRequest);
        }
}