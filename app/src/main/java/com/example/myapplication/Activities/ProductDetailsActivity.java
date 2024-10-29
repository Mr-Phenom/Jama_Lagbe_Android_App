package com.example.myapplication.Activities;

import static com.example.myapplication.MainActivity.ip_address;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SharedMemory;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    TextView textViewTitle,textViewDescription,textViewPrice,textViewCondition,textViewStatus,textViewUploader,textViewCategory
            ,textViewDate;
    String itemId,title,description,price,condition,status,uploader,category,date,photo,availableFor;
    ImageView imageViewPhoto;
    Button buttonAction,buttonWishlist;
    String type,transactionType,statusUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        textViewTitle = findViewById(R.id.textViewProductDetailsTitle);
        textViewDescription = findViewById(R.id.textViewProductDetailsDescription);
        textViewPrice = findViewById(R.id.textViewProductDetailsPrice);
        textViewCondition = findViewById(R.id.textViewProductDetailsCondition);
        textViewStatus = findViewById(R.id.textViewProductDetailsAvailibility);
        textViewUploader = findViewById(R.id.textViewProductDetailsPostedBy);
        textViewCategory = findViewById(R.id.textViewProductDetailsCategory);
        textViewDate = findViewById(R.id.textViewProductDetailsDate);

        imageViewPhoto = findViewById(R.id.imageViewProductDetailsPhoto);

        buttonAction = findViewById(R.id.buttonProductDetailsAction);
        buttonWishlist = findViewById(R.id.buttonProductDetailsWishlist);



        Intent intent = getIntent();
        itemId = intent.getStringExtra("item_id");
        title = intent.getStringExtra("title");
        description = intent.getStringExtra("description");
        price = intent.getStringExtra("price")+" tk";
        condition = intent.getStringExtra("conditions");
        status = intent.getStringExtra("status");
        uploader = intent.getStringExtra("user_mail");
        category = intent.getStringExtra("category");
        date = intent.getStringExtra("date_added").substring(0,10);
        availableFor = intent.getStringExtra("availableFor");

        photo = intent.getStringExtra("photo");


        textViewTitle.setText(title);
        textViewDescription.setText(description);
        textViewPrice.setText(price);
        textViewCategory.setText(category);
        textViewCondition.setText(condition);
        textViewDate.setText(date);
        textViewUploader.setText(uploader);
        textViewStatus.setText(status);

        Glide.with(this).load("http:/"+ip_address+"/"+photo).into(imageViewPhoto);

        if(availableFor.equals("sale"))
        {
            buttonAction.setText("Buy");
            type = "Buy";
            transactionType="buy";
            statusUpdate = "sold";
        } else if (availableFor.equals("rent")) {
            buttonAction.setText("Borrow");
            type="Borrow";
            transactionType="borrow";
            statusUpdate = "sold";
        }
        else
        {
            buttonAction.setText("Get Donation");
            type = "Get Donation";
            transactionType="donation";
            statusUpdate = "donated";
        }

        if(status.equals("sold"))
        {
            textViewStatus.setTextColor(Color.RED);
            buttonAction.setVisibility(View.INVISIBLE);
            if(availableFor.equals("sale")||availableFor.equals("donation"))
            {
                buttonWishlist.setVisibility(View.INVISIBLE);
            }
        }
        else if(status.equals("available"))
        {
            textViewStatus.setTextColor(Color.GREEN);
        }


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String buyerEmail = sharedPreferences.getString("email","");

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                builder.setTitle("Confirm Purchase");
                builder.setMessage("Are you sure you want to "+type+" "+ title+" for "+price+"?");
                builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        completePurchase(buyerEmail);
                    }
                });
                builder.setNegativeButton("Cancel",(dialog, which) -> dialog.dismiss());
                builder.show();
            }
        });

        buttonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndAddToWishlist(buyerEmail,itemId);
            }
        });

    }

    public void checkAndAddToWishlist(String buyerEmail,String itemId)
    {
        String url = "http://" + ip_address + "/app_check_wishlist.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            if (response.equals("already_added")) {
                Toast.makeText(this, "Already added to Wishlist", Toast.LENGTH_SHORT).show();
            } else if (response.equals("added")) {
                Toast.makeText(this, "Item added to Wishlist", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Error adding to Wishlist. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", itemId);
                params.put("user_email", buyerEmail);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void completePurchase(String buyerEmail)
    {
        String url = "http://"+ip_address+"/app_purchase_item.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("ServerResponse", response);
            if (response.equals("success")) {
                Toast.makeText(this, "Purchase successful!", Toast.LENGTH_SHORT).show();
                // Set result to indicate that the item has been bought
                Intent resultIntent = new Intent();
                resultIntent.putExtra("item_id", itemId);  // Send back item ID if needed
                setResult(RESULT_OK, resultIntent);

                finish();
            } else {
                Toast.makeText(this, "Purchase failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }, error -> {
            Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("item_id", String.valueOf(itemId));
                params.put("buyer_email", buyerEmail);
                params.put("transaction_type", transactionType);
                //params.put("status", statusUpdate);
                params.put("date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}