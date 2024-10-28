package com.example.myapplication.Adapter;

import static com.example.myapplication.MainActivity.ip_address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.ModelClass.ListItem;
import com.example.myapplication.R;

import java.util.Collections;
import java.util.List;

public class AdapterClassListItem extends RecyclerView.Adapter<AdapterClassListItem.ViewHolder> {

    private Context context;
    private List<ListItem> itemList;

    public AdapterClassListItem(Context context, List<ListItem> itemList) {
        this.context = context;
        this.itemList = itemList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_design_listing_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListItem item = itemList.get(position);

        holder.title.setText(item.getTitle());
        holder.category.setText(item.getCategory());
        holder.price.setText(item.getPrice());

        // Load image (use a library like Picasso or Glide for image loading)
        Glide.with(context)
                .load("http://"+ip_address+"/" + item.getPicture()).into(holder.image);
        if(item.getAvailableFor().equals("sale"))
        {
            holder.button.setText("Buy");
        }
        else if(item.getAvailableFor().equals("rent"))
        {
            holder.button.setText("Borrow");
        }
        else
        {
            holder.button.setText("Get Donation");
        }

        holder.button.setOnClickListener(v -> {
            // Handle button click
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, category, price;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageViewCardListItem);
            title = itemView.findViewById(R.id.textViewCardListItemTitle);
            category = itemView.findViewById(R.id.textViewCardListItemCategory);
            price = itemView.findViewById(R.id.textViewCardListItemPrice);
            button = itemView.findViewById(R.id.buttonCardListItem);
        }
    }
}

