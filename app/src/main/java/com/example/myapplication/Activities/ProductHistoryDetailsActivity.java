package com.example.myapplication.Activities;

import static com.example.myapplication.MainActivity.ip_address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductHistoryDetailsActivity extends AppCompatActivity {

    TextView textViewTitle,textViewDescription,textViewType,textViewCategory,textViewPrice,textViewSellerName,textViewBuyerName,textViewStartDate,
            textViewEndDate;
    Button buttonAction;
    ImageView imageView;
    String type,id,userEmail,photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_history_details);

        textViewTitle = findViewById(R.id.textViewProductHistoryDetailsTitle);
        textViewDescription = findViewById(R.id.textViewProductHistoryDetailsDescription);
        textViewCategory = findViewById(R.id.textViewProductHistoryDetailsCatergory);
        textViewPrice = findViewById(R.id.textViewProductHistoryDetailsPrice);
        textViewSellerName = findViewById(R.id.textViewProductHistoryDetailsSellerName);
        textViewBuyerName = findViewById(R.id.textViewProductHistoryDetailsBuyerName);
        textViewStartDate = findViewById(R.id.textViewProductHistoryDetailsStartDate);
        textViewEndDate = findViewById(R.id.textViewProductHistoryDetailsEndDate);

        buttonAction = findViewById(R.id.buttonProductHistoryDetails);

        imageView = findViewById(R.id.imageViewProductHistoryDetails);


        Intent intent = getIntent();
        type=intent.getStringExtra("type");
        id=intent.getStringExtra("id");


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail= sharedPreferences.getString("email","");

        fetchProductDetails(type, id);
        //Glide.with(this).load("http:/"+ip_address+"/"+photo).into(imageView);

        if(type.equals("Buy")||type.equals("Sold"))
        {
            buttonAction.setVisibility(View.INVISIBLE);
            textViewBuyerName.setVisibility(View.VISIBLE);
            textViewStartDate.setVisibility(View.VISIBLE);
            textViewEndDate.setVisibility(View.VISIBLE);
        }
        else {
            buttonAction.setText("Delete This Listing");
            buttonAction.setVisibility(View.VISIBLE);
            textViewBuyerName.setVisibility(View.INVISIBLE);
            textViewStartDate.setVisibility(View.INVISIBLE);
            textViewEndDate.setVisibility(View.INVISIBLE);

            buttonAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete code
                    confirmDelete();
                }
            });
        }

    }

    private void fetchProductDetails(String type, String id) {
        String url = "http://" + ip_address + "/app_fetch_product_history_details.php?type=" + type + "&item_id=" + id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            textViewTitle.setText(response.getString("title"));
                            textViewDescription.setText(response.getString("description"));
                            textViewCategory.setText(response.getString("category"));
                            textViewPrice.setText(response.getString("price"));
                            textViewSellerName.setText(response.getString("seller_name"));
                            textViewBuyerName.setText(response.optString("buyer_name", "N/A"));
                            textViewStartDate.setText(response.optString("start_date", "N/A"));
                            textViewEndDate.setText(response.optString("end_date", "N/A"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProductHistoryDetailsActivity.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductHistoryDetailsActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }

    private void confirmDelete() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Delete Listing")
                .setMessage("Are you sure you want to delete this listing?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteListing(id);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void deleteListing(String itemId) {
        String url = "http://" + ip_address + "/app_delete_product.php?item_id=" + itemId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("success")) {
                            Toast.makeText(ProductHistoryDetailsActivity.this, "Listing deleted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ProductHistoryDetailsActivity.this, "Failed to delete listing", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductHistoryDetailsActivity.this, "Error deleting listing", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(request);
    }
}

