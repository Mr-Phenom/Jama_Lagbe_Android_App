package com.example.myapplication.Fragment;

import static com.example.myapplication.MainActivity.ip_address;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.AdapterClassListItem;
import com.example.myapplication.ModelClass.ListItem;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdapterClassListItem adapter;
    private List<ListItem> itemList;

    private List<String> selectedTypes = new ArrayList<>();
    private List<String> selectedStatus = new ArrayList<>();
    private List<String> selectedCondition = new ArrayList<>();
    private String minPrice = "";
    private String maxPrice = "";

    public HomeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        recyclerView=view.findViewById(R.id.recyclerViewHome);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));

        itemList=new ArrayList<>();

        adapter=new AdapterClassListItem(getContext(),itemList);
        recyclerView.setAdapter(adapter);

        fetchData();

        return view;
    }

    public void applyFilters(List<String> types,List<String> status,List<String> condition, String minPrice, String maxPrice) {
        // Update filter values
        this.selectedTypes = types;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.selectedStatus=status;
        this.selectedCondition=condition;



        // Fetch filtered data
        fetchData();
    }

    private void fetchData() {
        StringBuilder urlBuilder = new StringBuilder("http://").append(ip_address).append("/app_fetch_clothingitems.php");

        boolean isFirstParam = true;

        // Add filters as query parameters if available
        if (!selectedTypes.isEmpty()) {
            urlBuilder.append(isFirstParam ? "?" : "&").append("types=").append(TextUtils.join(",", selectedTypes));
            isFirstParam = false; // After first parameter, next ones need '&'
        }
        if (!selectedCondition.isEmpty()) {
            urlBuilder.append(isFirstParam ? "?" : "&").append("conditions=").append(TextUtils.join(",", selectedCondition));
            isFirstParam = false; // After first parameter, next ones need '&'
        }

        // Add status filter
        if (!selectedStatus.isEmpty()) {
            urlBuilder.append(isFirstParam ? "?" : "&").append("status=").append(TextUtils.join(",", selectedStatus));
            isFirstParam = false;
        }

        // Add minPrice filter
        if (!minPrice.isEmpty()) {
            urlBuilder.append(isFirstParam ? "?" : "&").append("minPrice=").append(minPrice);
            isFirstParam = false;
        }

        // Add maxPrice filter
        if (!maxPrice.isEmpty()) {
            urlBuilder.append(isFirstParam ? "?" : "&").append("maxPrice=").append(maxPrice);
        }

        String url = urlBuilder.toString();
        Log.d("HomeFragment", "Fetching data with URL: " + url);

        // Fetch the data using the updated URL with filters
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray array = new JSONArray(response);
                    itemList.clear();  // Clear previous items before adding new ones
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject item = array.getJSONObject(i);

                        String picture = item.optString("photo", null);
                        String title = item.optString("title", "No Title");
                        String category = item.optString("category", "No Category");
                        String price = item.optString("price", "0");
                        String availableFor = item.optString("available_for", "not available");

                        ListItem listItem = new ListItem(picture, title, category, price, availableFor);
                        itemList.add(listItem);
                    }

                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }


}