package com.openclassrooms.oc_p7.views.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.databinding.ItemLayoutRestaurantBinding;


public class RestaurantViewHolder extends RecyclerView.ViewHolder {

    public final ItemLayoutRestaurantBinding itemLayoutRestaurantBinding;

    public RestaurantViewHolder(ItemLayoutRestaurantBinding itemLayoutRestaurantBinding) {
        super(itemLayoutRestaurantBinding.getRoot());
        this.itemLayoutRestaurantBinding = itemLayoutRestaurantBinding;
    }


}
