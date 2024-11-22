package com.example.myapplication.Activities;

import static com.example.myapplication.MainActivity.ip_address;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.AdapterClassListHistory;
import com.example.myapplication.Adapter.AdapterClassListItem;
import com.example.myapplication.ModelClass.HistoryItem;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryDetailsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textViewDetailsType;
    String type,userEmail;
    List<HistoryItem> itemList;
    AdapterClassListHistory adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);

        recyclerView = findViewById(R.id.recyclerViewHistoryDetails);
        textViewDetailsType = findViewById(R.id.textViewHistoryDetailsType);

        Intent intent = getIntent();
        type = intent.getStringExtra("historyType");
        textViewDetailsType.setText(type);

        itemList = new ArrayList<>();
        adapter = new AdapterClassListHistory(this,itemList,type);

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail= sharedPreferences.getString("email","");

        fetchHistoryDetails(type,userEmail);
    }

    private void fetchHistoryDetails(final String type, String mail) {
        String url = "http://" + ip_address + "/app_fetch_history_details.php?type=" + type + "&user_email=" + mail;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        itemList.clear();
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject item = response.getJSONObject(i);
                                String id = item.getString("item_id");
                                String title = item.getString("title");
                                String imageUrl = item.getString("photo");

                                // ItemType is the category from clothingitems
                                String itemType = item.getString("itemType");

                                // Status varies based on the type
                                String status = item.getString("status");

                                if(type.equals("Sold"))
                                {
                                    if(status.equals("buy"))
                                        status="Sold";
                                    if (status.equals("donation"))
                                        status="Donated";
                                    if (status.equals("borrow"))
                                        status="Rented";
                                }

                                //Log.d("Failed to load data", "onResponse: "+id);
                                // Add item to the list
                                itemList.add(new HistoryItem(id,title, itemType, status, imageUrl));
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(HistoryDetailsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                            Log.d("Failed to load data", "Fail of historyDetails: " + e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HistoryDetailsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Log.d("Failed to load data", "Fail of historyDetails: " + error);
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

}