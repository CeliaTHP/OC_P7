package com.openclassrooms.oc_p7.views.adapters;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.callbacks.OnRestaurantClickListener;
import com.openclassrooms.oc_p7.databinding.ItemLayoutRestaurantBinding;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.view_models.MapViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder.ViewHolder> {

    private final static String TAG = "RestaurantAdapter";

    private final OnRestaurantClickListener onRestaurantClickListener;

    private List<Restaurant> restaurantList;
    private Location currentLocation;
    private Context context;
    private MapViewModel mapViewModel;

    public RestaurantAdapter(List<Restaurant> restaurantList, Location currentLocation, OnRestaurantClickListener onRestaurantClickListener, MapViewModel mapViewModel) {
        this.restaurantList = restaurantList;
        this.currentLocation = currentLocation;
        this.onRestaurantClickListener = onRestaurantClickListener;
        this.mapViewModel = mapViewModel;

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
        Log.d(TAG, "onBind :  " + restaurant.toString());

        if (!restaurant.getHasDetails()) {
            mapViewModel.updateRestaurantDetails(restaurant);
        }

        Location location = new Location("");
        location.setLatitude(restaurant.getLat());
        location.setLongitude(restaurant.getLng());

        holder.itemLayoutRestaurantBinding.itemRestaurantName.setText(restaurant.getName());
        holder.itemLayoutRestaurantBinding.itemRestaurantTypeAndAddress.setText(holder.itemView.getContext().getString(R.string.item_restaurant_address, restaurant.getAddress()));

        holder.itemLayoutRestaurantBinding.itemRestaurantRating.setStepSize(0.01f);
        holder.itemLayoutRestaurantBinding.itemRestaurantRating.setRating((float) (restaurant.getRating() * 3.0 / 5.0));
        holder.itemLayoutRestaurantBinding.itemRestaurantRating.invalidate();
        holder.itemLayoutRestaurantBinding.itemRestaurantDistance.setText(holder.itemView.getContext().getString(R.string.item_restaurant_distance, location.distanceTo(currentLocation)));
        holder.itemLayoutRestaurantBinding.itemRestaurantHours.setText(setCorrespondingHours(restaurant));
        if (restaurant.getAttendees() != null)
            holder.itemLayoutRestaurantBinding.itemRestaurantAttendees.setText(holder.itemView.getContext().getString(R.string.item_restaurant_attendees, restaurant.getAttendees().size()));

        if (restaurant.getPhotoReferences() != null) {
            String picUrl = holder.itemView.getContext().getString(R.string.place_photo_url, holder.itemLayoutRestaurantBinding.getRoot().getContext().getString(R.string.GOOGLE_MAP_API_KEY_DEV), restaurant.getPhotoReferences().get(0));
            //Blinks with picasso too

            /*
            Picasso.get()
                    .load(picUrl)
                    .resize(100,100)
                    .centerCrop()
                    .into(holder.itemLayoutRestaurantBinding.itemRestaurantPic);

             */

            Glide.with(holder.itemView.getContext())
                    .load(picUrl)
                    .centerCrop()
                    .dontAnimate()
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(holder.itemLayoutRestaurantBinding.itemRestaurantPic);


        } else {
            holder.itemLayoutRestaurantBinding.itemRestaurantPic.setImageResource(R.drawable.ic_cutlery);
        }

        holder.itemLayoutRestaurantBinding.getRoot().setOnClickListener(v -> onRestaurantClickListener.onRestaurantClick(restaurant));


    }

    private String setCorrespondingHours(Restaurant restaurant) {
        String hours = null;
        if (restaurant.getOpeningHours() == null)
            return context.getString(R.string.item_restaurant_no_info);
        else {
            switch (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                case Calendar.MONDAY:
                    hours = restaurant.getOpeningHours().get(0);
                    break;
                case Calendar.TUESDAY:
                    hours = restaurant.getOpeningHours().get(1);
                    break;
                case Calendar.WEDNESDAY:
                    hours = restaurant.getOpeningHours().get(2);
                    break;
                case Calendar.THURSDAY:
                    hours = restaurant.getOpeningHours().get(3);
                    break;
                case Calendar.FRIDAY:
                    hours = restaurant.getOpeningHours().get(4);
                    break;
                case Calendar.SATURDAY:
                    hours = restaurant.getOpeningHours().get(5);
                    break;
                case Calendar.SUNDAY:
                    hours = restaurant.getOpeningHours().get(6);
                    break;
            }
        }
        return hours;
    }

    public void setData(List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }


    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
