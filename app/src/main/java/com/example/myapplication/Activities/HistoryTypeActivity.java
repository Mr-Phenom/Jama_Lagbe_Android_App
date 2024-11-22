package com.example.myapplication.Activities;

import static com.example.myapplication.MainActivity.ip_address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HistoryTypeActivity extends AppCompatActivity {
    TextView textViewActive,textViewSold,textViewBuy;
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_type);

        textViewActive = findViewById(R.id.textViewHistoryTypeActive);
        textViewSold = findViewById(R.id.textViewHistoryTypeSold);
        textViewBuy = findViewById(R.id.textViewHistoryTypeBuy);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail= sharedPreferences.getString("email","");

        fetchHistoryData(userEmail);


        textViewActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryTypeActivity.this,HistoryDetailsActivity.class);
                intent.putExtra("historyType","Active");
                startActivity(intent);

            }
        });

        textViewSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryTypeActivity.this,HistoryDetailsActivity.class);
                intent.putExtra("historyType","Sold");
                startActivity(intent);
            }
        });

        textViewBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryTypeActivity.this,HistoryDetailsActivity.class);
                intent.putExtra("historyType","Buy");
                startActivity(intent);
            }
        });

    }


    private void fetchHistoryData(String email) {
        String url = "http://"+ip_address+"/app_history_type.php";  // Replace with your server URL
        Log.d("fetchDataHistory", "fetchHistoryData: "+email);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("fetchDataHistory", "fetchHistoryData is in onResponse ");
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int active = jsonObject.getInt("active");
                            int sold = jsonObject.getInt("sold");
                            int bought = jsonObject.getInt("bought");

                            Log.d("fetchDataHistory", "active number : "+active);
                            // Update TextViews
                            textViewActive.setText("Your Active Listings(" + active+")");
                            textViewSold.setText("Your Sold Listings(" + sold+")");
                            textViewBuy.setText("Your All Purchase(" + bought+")");
                        } catch (JSONException e) {
                            Toast.makeText(HistoryTypeActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String errorMessage = new String(error.networkResponse.data);
                    Toast.makeText(HistoryTypeActivity.this, "Failed to fetch data: " + errorMessage, Toast.LENGTH_LONG).show();
                    Log.d("fetchDataHistory", "error is : "+ errorMessage);
                } else {
                    Toast.makeText(HistoryTypeActivity.this, "Failed to fetch data: " + error.toString(), Toast.LENGTH_LONG).show();
                    Log.d("fetchDataHistory", "error is : "+ error.toString());
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", email);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
}