package com.example.myapplication.Fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.example.myapplication.MainActivity.ip_address;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.AdapterClassListItem;
import com.example.myapplication.ModelClass.ListItem;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FavouritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterClassListItem adapter;
    private List<ListItem> wishList;
    private String currUser="";

    private ActivityResultLauncher<Intent> productDetailsLauncher;

    public FavouritesFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_favourites, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSession", MODE_PRIVATE);
        currUser=sharedPreferences.getString("email","");

        productDetailsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                         // Refresh data to reflect the purchase status
                        fetchData();
                    }
                }
        );

        recyclerView = view.findViewById(R.id.recyclerViewFavourite);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        wishList = new ArrayList<>();

        adapter = new AdapterClassListItem(getContext(),wishList,productDetailsLauncher);
        recyclerView.setAdapter(adapter);


        fetchData();
       return view;
    }

    public void fetchData()
    {
        String url = "http://"+ip_address+"/app_fetch_wishlist.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            try {
                Log.d("wishlist check","it is here try");
                JSONArray array = new JSONArray(response);
                wishList.clear();  // Clear previous items before adding new ones
                for (int i = 0; i < array.length(); i++) {
                    Log.d("wishlist check","it is here for loop");
                    JSONObject item = array.getJSONObject(i);

                    String itemId = item.optString("item_id", "0");
                    String uploader = item.optString("user_email", "not given");
                    String date = item.optString("date_added", "not given");
                    String condition = item.optString("conditions", "not given");
                    String status = item.optString("status", "not given");
                    String picture = item.optString("photo", null);
                    String title = item.optString("title", "No Title");
                    String category = item.optString("category", "No Category");
                    String price = item.optString("price", "0");
                    String availableFor = item.optString("available_for", "not available");
                    String description = item.optString("description", "not given");

                    // Add each wishlist item to the list without uploader filter
                    ListItem listItem = new ListItem(itemId, picture, title, description, category, price, availableFor, uploader, date, condition, status);
                    wishList.add(listItem);
                    Log.d("wishlist check"," "+wishList.size());
                }

                adapter.notifyDataSetChanged();

            } catch (JSONException e) {
                Log.d("wishlist check","it is here error");
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_email", currUser); // Pass the current user email to fetch wishlist items
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
}