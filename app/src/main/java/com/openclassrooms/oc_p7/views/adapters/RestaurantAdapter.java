package com.openclassrooms.oc_p7.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.databinding.ItemLayoutRestaurantBinding;
import com.openclassrooms.oc_p7.models.Restaurant;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder.ViewHolder> {

    private List<Restaurant> restaurantList;
    private Context context;

    public RestaurantAdapter(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        ItemLayoutRestaurantBinding itemLayoutRestaurantBinding = ItemLayoutRestaurantBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new RestaurantViewHolder.ViewHolder(itemLayoutRestaurantBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RestaurantViewHolder.ViewHolder holder, int position) {
        Restaurant restaurant = restaurantList.get(position);
        if (restaurant != null)
            holder.itemLayoutRestaurantBinding.itemRestaurantName.setText(restaurant.getName());


    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
//TODO ADAPTER
}
