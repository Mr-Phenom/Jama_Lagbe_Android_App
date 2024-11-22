package com.example.myapplication.Fragment;


import static com.example.myapplication.MainActivity.ip_address;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.NotificationAdapter;
import com.example.myapplication.ModelClass.NotificationItem;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private TextView textViewNoNotifications;
    private List<NotificationItem> notificationList = new ArrayList<>(); // Initialize here

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recyclerView_notifications);
        textViewNoNotifications = view.findViewById(R.id.textView_noNotifications);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationList, getContext());
        recyclerView.setAdapter(adapter);

        // Fetch notifications
        fetchNotificationsFromDatabase();

        return view;
    }

    private void fetchNotificationsFromDatabase() {
        // URL of your PHP script to fetch notifications
        String url = "http://"+ip_address+"/app_fetch_notification.php";

        // Use Volley to make the network request
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                response -> {
                    try {
                        Log.d("NotificationFragment logs", "Response: " + response.toString());
                        notificationList.clear(); // Clear old data
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject notificationObj = response.getJSONObject(i);

                            NotificationItem item = new NotificationItem();
                            item.setNotificationId(Integer.parseInt(notificationObj.getString("notification_id")));
                            item.setItemId(Integer.parseInt(notificationObj.getString("item_id")));
                            item.setTransactionId(Integer.parseInt(notificationObj.getString("transaction_id"))); // Ensure transaction_id is fetched
                            item.setNotificationText(notificationObj.getString("notification_text"));

                           // item.setTransactionId(15);

                            notificationList.add(item);
                        }

                        // Update UI
                        if (notificationList.isEmpty()) {
                            textViewNoNotifications.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            textViewNoNotifications.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        adapter.notifyDataSetChanged(); // Notify adapter of data changes
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Error parsing notifications.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(getContext(), "Error fetching notifications.", Toast.LENGTH_SHORT).show()
        );

        requestQueue.add(jsonArrayRequest);
    }
}
