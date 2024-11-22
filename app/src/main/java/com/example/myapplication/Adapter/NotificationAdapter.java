package com.example.myapplication.Adapter;

import static com.example.myapplication.MainActivity.ip_address;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.ModelClass.NotificationItem;
import com.example.myapplication.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<NotificationItem> notificationList;

    public NotificationAdapter(Context context, List<NotificationItem> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    public NotificationAdapter(List<NotificationItem> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }


    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationItem notification = notificationList.get(position);
        holder.notificationText.setText(notification.getNotificationText());

        // Handle return button click
        holder.returnButton.setOnClickListener(v -> {
            markItemAsReturned(notification);
            notificationList.remove(position);
            notifyItemRemoved(position);
            Toast.makeText(context, "Item returned and notification removed.", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView notificationText;
        Button returnButton;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationText = itemView.findViewById(R.id.notificationText);
            returnButton = itemView.findViewById(R.id.returnButton);
        }
    }

    private void markItemAsReturned(NotificationItem notification) {
        // URL of your PHP script to mark the item as returned
        String url = "http://"+ip_address+"/app_mark_item_as_returned.php";

        // Create a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        // Send a POST request with transaction_id
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                response -> {
                    if (response.trim().equals("success")) {
                        Toast.makeText(context, "Item marked as returned successfully.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to mark item as returned.", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(context, "Error communicating with server.", Toast.LENGTH_SHORT).show()
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("transaction_id", String.valueOf(notification.getTransactionId()));
                return params;
            }
        };

        // Add the request to the queue
        requestQueue.add(stringRequest);
    }

}

