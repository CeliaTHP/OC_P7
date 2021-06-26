package com.openclassrooms.oc_p7.views.adapters;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ItemLayoutRestaurantBinding;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder.ViewHolder> {

    private final static String TAG = "RestaurantAdapter";
    private List<RestaurantPojo> restaurantList;
    private Location currentLocation;
    private Context context;

    //TODO : RESTAURANT LIST !
    public RestaurantAdapter(List<RestaurantPojo> restaurantList, Location currentLocation) {
        this.restaurantList = restaurantList;
        this.currentLocation = currentLocation;
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

            Location location = new Location("");
            location.setLatitude(restaurant.geometry.location.lat);
            location.setLongitude(restaurant.geometry.location.lng);


            holder.itemLayoutRestaurantBinding.itemRestaurantTypeAndAddress.setText(holder.itemView.getContext().getString(R.string.item_restaurant_address, restaurant.vicinity));
            holder.itemLayoutRestaurantBinding.itemRestaurantName.setText(restaurant.name);

            holder.itemLayoutRestaurantBinding.itemRestaurantRating.setStepSize(0.01f);
            holder.itemLayoutRestaurantBinding.itemRestaurantRating.setRating((float) (restaurant.rating * 3.0 / 5.0));
            holder.itemLayoutRestaurantBinding.itemRestaurantRating.invalidate();
            holder.itemLayoutRestaurantBinding.itemRestaurantDistance.setText(holder.itemView.getContext().getString(R.string.item_restaurant_distance, location.distanceTo(currentLocation)));
            // holder.itemLayoutRestaurantBinding.itemRestaurantHours.setText(setCorrespondingHours(restaurant));

            if (restaurant.photos != null) {

                String picUrl = holder.itemView.getContext().getString(R.string.place_photo_url, BuildConfig.GoogleMapApiKey, restaurant.photos.get(0).photo_reference);
                Log.d(TAG, picUrl);
                Glide.with(holder.itemView.getContext())
                        .load(picUrl)
                        .centerCrop()
                        .into(holder.itemLayoutRestaurantBinding.itemRestaurantPic);

            } else {
                holder.itemLayoutRestaurantBinding.itemRestaurantPic.setImageResource(R.drawable.ic_cutlery);
            }
        }


    }


    private String setCorrespondingHours(RestaurantPojo restaurantPojo) {
        String hours = null;
        switch (Calendar.DAY_OF_WEEK) {
            case Calendar.MONDAY:
                break;
        }
        return hours;
    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
//TODO ADAPTER
}
