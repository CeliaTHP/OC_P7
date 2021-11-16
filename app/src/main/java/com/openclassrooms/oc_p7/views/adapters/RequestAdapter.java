package com.openclassrooms.oc_p7.views.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.ItemLayoutRequestBinding;
import com.openclassrooms.oc_p7.models.Restaurant;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder.ViewHolder> {

    private List<Restaurant> restaurantList;
    private Boolean inDetails;
    private final OnRestaurantClickListener onRestaurantClickListener;


    public RequestAdapter(List<Restaurant> restaurantList, OnRestaurantClickListener onRestaurantClickListener) {
        this.restaurantList = restaurantList;
        this.onRestaurantClickListener = onRestaurantClickListener;
    }

    @NonNull
    @Override
    public RequestViewHolder.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLayoutRequestBinding itemLayoutRequestBinding = ItemLayoutRequestBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new RequestViewHolder.ViewHolder(itemLayoutRequestBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder.ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        holder.itemLayoutRequestBinding.itemRequestName.setText(restaurant.getName());
        holder.itemLayoutRequestBinding.itemRequestAddress.setText(restaurant.getAddress());
        holder.itemLayoutRequestBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRestaurantClickListener.onRestaurantClick(restaurant);
            }
        });


    }

    public void setData(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
