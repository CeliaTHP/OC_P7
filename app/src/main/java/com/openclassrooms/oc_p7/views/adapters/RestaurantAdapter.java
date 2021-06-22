package com.openclassrooms.oc_p7.views.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ItemLayoutRestaurantBinding;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder.ViewHolder> {

    private final static String TAG = "RestaurantAdapter";
    private List<RestaurantPojo> restaurantList;
    private Context context;

    public RestaurantAdapter(List<RestaurantPojo> restaurantList) {
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
        RestaurantPojo restaurant = restaurantList.get(position);
        if (restaurant != null) {
            holder.itemLayoutRestaurantBinding.itemRestaurantTypeAndAddress.setText(holder.itemView.getContext().getString(R.string.item_restaurant_address, restaurant.vicinity));
            holder.itemLayoutRestaurantBinding.itemRestaurantName.setText(restaurant.name);

            //TODO : PARSE TO GET URL
            //TODO : https://developers.google.com/maps/documentation/places/web-service/photos
            /*
                Glide.with(holder.itemView.getContext())
                        .load(restaurant.photos.get(0).html_attributions)
                        .centerCrop()
                        .into(holder.itemLayoutRestaurantBinding.itemRestaurantPic);


             */
        }

    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
//TODO ADAPTER
}
