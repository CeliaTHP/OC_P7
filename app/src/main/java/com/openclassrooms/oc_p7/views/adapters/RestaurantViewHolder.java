package com.openclassrooms.oc_p7.views.adapters;

import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.databinding.ItemLayoutRestaurantBinding;

public class RestaurantViewHolder {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final ItemLayoutRestaurantBinding itemLayoutRestaurantBinding;

        public ViewHolder(ItemLayoutRestaurantBinding itemLayoutRestaurantBinding) {
            super(itemLayoutRestaurantBinding.getRoot());
            this.itemLayoutRestaurantBinding = itemLayoutRestaurantBinding;
        }
    }

}
