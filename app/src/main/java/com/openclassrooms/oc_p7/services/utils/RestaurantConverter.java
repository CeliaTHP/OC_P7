package com.openclassrooms.oc_p7.services.utils;

import android.location.Location;
import android.util.Log;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;

import java.util.ArrayList;
import java.util.List;

public class RestaurantConverter {


    public static List<Restaurant> transformToRestaurants(List<RestaurantPojo> restaurantPojos, Location location) {
        String TAG = "transformToRestaurants";
        List<Restaurant> restaurantList = new ArrayList<>();
        for (RestaurantPojo restaurantPojo : restaurantPojos) {
            restaurantList.add(createRestaurant(restaurantPojo, location));

        }
        Log.d(TAG, "size : " + restaurantList.size());
        return restaurantList;
    }


    public void setWorkmates() {

    }

    static Restaurant createRestaurant(RestaurantPojo restaurantPojo, Location userLocation) {
        Restaurant restaurant = new Restaurant(restaurantPojo.place_id, restaurantPojo.name, restaurantPojo.vicinity, restaurantPojo.geometry.location.lat, restaurantPojo.geometry.location.lng);
        Location location = new Location("");
        location.setLatitude(restaurant.getLat());
        location.setLongitude(restaurant.getLng());
        restaurant.setDistance(location.distanceTo(userLocation));
        Log.d("NEW_CONVERTER", restaurant.toString());
        return restaurant;

    }
}
