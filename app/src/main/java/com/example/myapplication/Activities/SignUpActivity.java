package com.example.myapplication.Activities;

import static com.example.myapplication.MainActivity.ip_address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    EditText name,phone,address,password,email;
    Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = findViewById(R.id.editTextNameSignUp);
        phone = findViewById(R.id.editTextPhoneSignup);
        address=findViewById(R.id.editTextAdressSignup);
        password=findViewById(R.id.editTextPasswordSignup);
        email=findViewById(R.id.editTextEmailSignup);
        signup = findViewById(R.id.buttonSignup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });

    }

    private void signUpUser()
    {
        String sName = name.getText().toString().trim();
        String sPhone = phone.getText().toString().trim();
        String sAddress = address.getText().toString().trim();
        String sEmail = email.getText().toString().trim();
        String sPassword = password.getText().toString().trim();

        if(sName.isEmpty() || sAddress.isEmpty() || sPhone.isEmpty() || sEmail.isEmpty() || sPassword.isEmpty())
        {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://"+ip_address+"/signup.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");

                    if (status.equals("success")) {
                        Intent intent = new Intent(SignUpActivity.this,PictureUploadActivity.class);
                        intent.putExtra("email",sEmail);
                        startActivity(intent);
                        Toast.makeText(SignUpActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (status.equals("email exist")){
                        Toast.makeText(SignUpActivity.this, "This email already exists", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, "Error parsing response", Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignUpActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send email and password to the server via POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("name", sName);
                params.put("email", sEmail);
                params.put("phone", sPhone);
                params.put("address", sAddress);// 'email' is the key sent to PHP
                params.put("password", sPassword);
                // 'password' is the key sent to PHP
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
        finish();



    }
}