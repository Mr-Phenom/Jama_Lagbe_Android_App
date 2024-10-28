package com.example.myapplication.Activities;

import static com.example.myapplication.Activities.PictureUploadActivity.IMAGE_REQUEST;
import static com.example.myapplication.MainActivity.ip_address;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SellRentActivity extends AppCompatActivity {


    private Bitmap bitmap;
    String[] availableForItems = {"sale","rent"};
    AutoCompleteTextView autoCompleteTextView;
    TextInputEditText editTextTitle,editTextDescription,editTextCategory,editTextCondition,editTextPrice;
    ImageView imageViewUpload;
    Button buttonPost;
    ArrayAdapter<String> adapterItem;
    String title,description,category,condition,availableFor,price;
    String userEmail,type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_rent);

        //getting user mail
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email","");

        type = getIntent().getStringExtra("type");

        editTextTitle = findViewById(R.id.editTextBuySellTitle);
        editTextDescription = findViewById(R.id.editTextBuySellDescription);
        editTextCategory=findViewById(R.id.editTextBuySellCategory);
        editTextCondition=findViewById(R.id.editTextBuySellCondition);
        editTextPrice = findViewById(R.id.editTextBuySellPrice);

        imageViewUpload = findViewById(R.id.imageViewBuySellUpload);

        buttonPost = findViewById(R.id.buttonBuySellPost);

        autoCompleteTextView =findViewById(R.id.auto_complete_txt_available_for);
        adapterItem = new ArrayAdapter<String>(this,R.layout.list_item,availableForItems);

        autoCompleteTextView.setAdapter(adapterItem);


        //if donation then disabling available for and pricing
        if(type.equals("donation"))
        {
            editTextPrice.setText(""+00);
            editTextPrice.setInputType(InputType.TYPE_NULL);
            editTextPrice.setClickable(false);
            autoCompleteTextView.setVisibility(View.INVISIBLE);
            availableFor = "donation";

        }


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                availableFor = adapterView.getItemAtPosition(i).toString();

            }
        });

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title = editTextTitle.getText().toString();
                description = editTextDescription.getText().toString();
                category=editTextCategory.getText().toString();
                condition=editTextCondition.getText().toString();
                price=editTextPrice.getText().toString();

                if (title.isEmpty() || description.isEmpty() || category.isEmpty() || condition.isEmpty() || price.isEmpty()) {
                    // Show a Toast if any field is empty
                    Toast.makeText(SellRentActivity.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (bitmap == null) {
                    // Show a Toast if no image is selected
                    Toast.makeText(SellRentActivity.this, "Please upload an image", Toast.LENGTH_SHORT).show();
                } else {
                    // If all fields are valid, proceed with the upload
                    uploadItem();
                }


            }
        });

        imageViewUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageSelect();
            }
        });
    }


    private void imageSelect()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageViewUpload.setImageBitmap(bitmap); // Display the image in the ImageView
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadItem()
    {

        String url = "http://"+ip_address+"/app_upload_item.php"; // Change to your IP address

        // Convert Bitmap to Base64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(SellRentActivity.this, "Item Listed Successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SellRentActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage); // Send the image
                params.put("email", userEmail);
                params.put("title",title );
                params.put("description",description );
                params.put("category",category );
                params.put("condition",condition );
                params.put("price",price );
                params.put("available_for",availableFor );
                params.put("status","available" );
                params.put("date_added",String.valueOf(System.currentTimeMillis()));// Send the user's email
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);

    }
}