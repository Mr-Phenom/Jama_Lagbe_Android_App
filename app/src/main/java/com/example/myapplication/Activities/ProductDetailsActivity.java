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
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
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
    String buyerEmail;
    int days;

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
        textViewPrice.setText(price+"/day");
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
        buyerEmail = sharedPreferences.getString("email","");

        /*buttonAction.setOnClickListener(new View.OnClickListener() {
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
        }); */

        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (buttonAction.getText().toString().equals("Borrow")) {
                    // Show a dialog to enter the number of days for borrowing
                    AlertDialog.Builder daysInputDialog = new AlertDialog.Builder(ProductDetailsActivity.this);
                    daysInputDialog.setTitle("Enter Borrow Duration");

                    // Set up an EditText in the dialog
                    final EditText input = new EditText(ProductDetailsActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    input.setHint("Enter days");
                    daysInputDialog.setView(input);

                    daysInputDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String daysString = input.getText().toString();

                            if (!daysString.isEmpty()) {
                                days = Integer.parseInt(daysString);

                                // Proceed to the confirmation dialog
                                showConfirmationDialog("Borrow", title, price, days);
                            } else {
                                // Notify the user if input is empty
                                Toast.makeText(ProductDetailsActivity.this, "Please enter the number of days", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    daysInputDialog.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    daysInputDialog.show();

                } else {
                    // Directly show the confirmation dialog for other actions
                    showConfirmationDialog(type, title, price, 0); // Pass 0 for days if not borrowing
                }
            }
        });

        buttonWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAndAddToWishlist(buyerEmail,itemId);
            }
        });

    }

    private void showConfirmationDialog(String type, String title, String price, int days) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
        builder.setTitle("Confirm Purchase");

        // Customize the message based on whether it's a borrow or another transaction
        if (type.equals("Borrow")) {
            builder.setMessage("Are you sure you want to borrow " + title + " for " + price + " for " + days + " days?");
        } else {
            builder.setMessage("Are you sure you want to " + type + " " + title + " for " + price + "?");
        }

        builder.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                completePurchase(buyerEmail);
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
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

                if (transactionType.equals("borrow")) {

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR,days);
                    params.put("borrow_duration", String.valueOf(days)); // assuming `days` is available here
                    //params.put("borrow_start_date", borrowStartDate); // set `borrowStartDate` as needed
                    params.put("borrow_end_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.getTime())); // set `borrowEndDate` as needed
                }
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }
}