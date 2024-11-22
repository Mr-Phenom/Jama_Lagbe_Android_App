package com.example.myapplication.Adapter;

import static com.example.myapplication.MainActivity.ip_address;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Activities.ProductHistoryDetailsActivity;
import com.example.myapplication.Activities.StartActivity;
import com.example.myapplication.R;
import com.example.myapplication.ModelClass.HistoryItem;

import java.util.List;

public class AdapterClassListHistory extends RecyclerView.Adapter<AdapterClassListHistory.ViewHolder> {

    private Context context;
    private List<HistoryItem> itemList;
    String type;

    public AdapterClassListHistory(Context context, List<HistoryItem> itemList,String type) {
        this.context = context;
        this.itemList = itemList;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_design_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = itemList.get(position);

        // Bind data to views
        holder.textViewTitle.setText(item.getTitle());
        holder.textViewType.setText(item.getType());
        holder.textViewStatus.setText(item.getStatus());

        // Load image using Picasso (or any other image loading library)
        Glide.with(context)
                .load("http://"+ip_address+"/" + item.getImageUrl()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ProductHistoryDetailsActivity.class);
                intent.putExtra("id",item.getId());
                intent.putExtra("type",type);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle, textViewType, textViewStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewHistoryItem);
            textViewTitle = itemView.findViewById(R.id.textViewHistoryItemTitle);
            textViewType = itemView.findViewById(R.id.textViewHistoryItemType);
            textViewStatus = itemView.findViewById(R.id.textViewHistoryItemStatus);
        }
    }
}
