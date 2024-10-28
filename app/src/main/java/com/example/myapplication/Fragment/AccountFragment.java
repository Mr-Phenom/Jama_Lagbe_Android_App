package com.example.myapplication.Fragment;

import static android.content.Context.MODE_PRIVATE;
import static com.example.myapplication.MainActivity.ip_address;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.DashboardActivity;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AccountFragment extends Fragment {


    private static final int IMAGE_REQUEST = 1;
   ImageView accountPicture ;
   TextView textViewName,textViewEmail,textViewAddress,textViewPhone;
   Button buttonUpdatePic,buttonUpdateProfile,buttonLogout;
    private Bitmap bitmap;
    String email;


    public static AccountFragment getInstance()
    {
        return new AccountFragment();
    }

    public AccountFragment()
    {

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_account,container,false);

        //connecting with ids
        accountPicture = view.findViewById(R.id.imageViewAccountPicture);
        buttonUpdatePic = view.findViewById(R.id.buttonUpdateProfilePic);
        buttonUpdateProfile = view.findViewById(R.id.buttonUpdateProfile);
        buttonLogout = view.findViewById(R.id.buttonLogout);
        textViewEmail = view.findViewById(R.id.textViewEmailAccount);
        textViewName = view.findViewById(R.id.textViewNameAccount);
        textViewAddress = view.findViewById(R.id.textViewAddressAccount);
        textViewPhone = view.findViewById(R.id.textViewPhoneAccount);

        //getting email from sharedpreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        email = sharedPreferences.getString("email","");
        setInfo();

        // Select image from gallery
        buttonUpdatePic.setOnClickListener(v -> openGallery());
        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logout();
            }
        });

        return view;

    }

    public void setInfo()
    {
        String url = "http://" + ip_address + "/app_get_user_info.php"; // Change ip_address accordingly

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.getBoolean("success")) {
                        // If the request is successful, set the text views with user info
                        String name = jsonObject.getString("name");
                        String address = jsonObject.getString("address");
                        String phone = jsonObject.getString("phone");
                        String imagePath = jsonObject.getString("picture");

                        Log.d("did u get it?",name);

                        textViewEmail.setText(email);
                       textViewName.setText(name);
                        textViewAddress.setText(address);
                        textViewPhone.setText(phone);

                        //Glide.with(getContext()).load("http://"+ip_address+"/"+imagePath).into(accountPicture);
                        loadImage(imagePath);
                    } else {
                        Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error parsing data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Send email to the PHP script
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        // Add request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    public void logout()
    {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all stored data
        editor.apply();

        // Navigate to login screen
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish(); // Close the dashboard activity
    }

    public void openGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
                accountPicture.setImageBitmap(bitmap); // Display the image in the ImageView

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        uploadImage();
    }

    private void uploadImage() {
        String url = "http://"+ip_address+"/app_upload_image.php"; // Change to your IP address

        // Convert Bitmap to Base64 encoded string
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getContext(), "Image Uploaded", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("image", encodedImage);
                params.put("email", email); // Send the user's email
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(stringRequest);
    }

    private void loadImage(String imagePath) {
        // Assuming the imagePath is a relative path, you need to prepend the base URL
        String fullPath = "http://" + ip_address + "/" + imagePath; // Adjust if necessary

        Glide.with(getContext())
                .load(fullPath) // Load the image from URL
                .into(accountPicture); // Set it into the ImageView
    }
}