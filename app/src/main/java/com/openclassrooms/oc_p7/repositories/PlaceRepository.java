package com.openclassrooms.oc_p7.repositories;

import android.location.Location;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.MyApplication;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.pojo_models.details.DetailsPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.details.RestaurantDetailsPojo;
import com.openclassrooms.oc_p7.models.pojo_models.general.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.models.pojo_models.general.Photo;
import com.openclassrooms.oc_p7.models.pojo_models.general.RestaurantPojo;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {

    private PlacesApi placesApi;
    private Executor executor;
    private String radiusQuery;
    private String restaurantQuery ;

    public PlaceRepository(PlacesApi placesApi,
                           Executor executor,
                           MutableLiveData<List<Restaurant>> restaurantLiveData,
                           String radiusQuery,
                           String restaurantQuery) {
        this.placesApi = placesApi;
        this.executor = executor;
        this.restaurantLiveData = restaurantLiveData;
        this.radiusQuery = radiusQuery;
        this.restaurantQuery = restaurantQuery;
    }

    private String TAG = "PlaceRepository";

    public MutableLiveData<List<Restaurant>> restaurantLiveData;

    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private int count = 0;


    public LiveData<List<Restaurant>> getRestaurantLiveData() {
        return restaurantLiveData;
    }

    public void getNearbyPlaces(Location location) {

        String locationStringQuery = location.getLatitude() + "," + location.getLongitude();

        executor.execute(() -> {
            Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(BuildConfig.GoogleMapApiKey, locationStringQuery, radiusQuery, restaurantQuery);
            try {
                Response<NearbyPlaceResponse> response = call.execute();
                if(response.isSuccessful()) {
                    NearbyPlaceResponse nearbyPlaceResponse = response.body();
                    if(nearbyPlaceResponse != null && nearbyPlaceResponse.restaurantPojos != null) {
                        List<Restaurant> restaurantList = getRestaurantList(nearbyPlaceResponse.restaurantPojos);
                        restaurantLiveData.postValue(restaurantList);
                    }
                } else {
                    //TODO postValue error
                }
            } catch (IOException e) {
                //TODO postValue error
            }
        });
    }

    public void getRestaurantDetails(String restaurantId, Restaurant restaurant, OnSuccessListener onSuccessListener) {
        Log.d("COUNT", count + " ");
        count++;
        placesApi.getDetailsById(BuildConfig.GoogleMapApiKey, restaurantId).enqueue(new Callback<DetailsPlaceResponse>() {
            @Override
            public void onResponse(Call<DetailsPlaceResponse> call, Response<DetailsPlaceResponse> response) {
                setRestaurantInfos(response.body().result, restaurant, onSuccessListener);
            }

            @Override
            public void onFailure(Call<DetailsPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                //TODO postValue error
            }

        });

    }

    //TODO static function
    static public List<Restaurant> getRestaurantList(List<RestaurantPojo> restaurantPojoList) {
        List<Restaurant> restaurantList = new ArrayList<>();
        for (RestaurantPojo restaurantPojo : restaurantPojoList) {
            Restaurant restaurant = createRestaurant(restaurantPojo);
            restaurantList.add(restaurant);
        }
        return restaurantList;
    }

    static private Restaurant createRestaurant(RestaurantPojo restaurantPojo) {
        return new Restaurant(restaurantPojo.place_id, restaurantPojo.name, restaurantPojo.vicinity, restaurantPojo.geometry.location.lat, restaurantPojo.geometry.location.lng);
    }

    private void setRestaurantInfos(RestaurantDetailsPojo restaurantDetailsPojo, Restaurant restaurant, OnSuccessListener<Restaurant> onSuccessListener) {

        if (restaurantDetailsPojo != null) {
            if (restaurantDetailsPojo.name != null)
                restaurant.setName(restaurantDetailsPojo.name);

            if (restaurantDetailsPojo.formatted_address != null)
                restaurant.setAddress(restaurantDetailsPojo.formatted_address);

            if (restaurantDetailsPojo.geometry.location.lat != 0.0)
                restaurant.setLat(restaurantDetailsPojo.geometry.location.lat);

            if (restaurantDetailsPojo.geometry.location.lng != 0.0)
                restaurant.setLng(restaurantDetailsPojo.geometry.location.lng);

            if (restaurantDetailsPojo.place_id != null)
                restaurant.setId(restaurantDetailsPojo.place_id);

            if (restaurantDetailsPojo.rating != 0.0)
                restaurant.setRating(restaurantDetailsPojo.rating);

            if (restaurantDetailsPojo.opening_hours != null)
                restaurant.setOpeningHours(restaurantDetailsPojo.opening_hours.weekday_text);

            if (restaurantDetailsPojo.international_phone_number != null)
                restaurant.setPhone(restaurantDetailsPojo.international_phone_number);

            if (restaurantDetailsPojo.website != null)
                restaurant.setWebsite(restaurantDetailsPojo.website);

            if (restaurantDetailsPojo.photos != null) {
                List<String> photos = new ArrayList<>();
                for (Photo photoUrl : restaurantDetailsPojo.photos) {
                    photos.add(photoUrl.photo_reference);
                }
                restaurant.setPhotoReference(photos);
            }
            onSuccessListener.onSuccess(restaurant);
            /*
            Log.d(TAG, restaurant.getName() + " " + restaurant.getRating() + " " + restaurant.getOpeningHours() + " " + restaurant.getPhone() + " "
                    + restaurant.getWebsite() + " " + restaurant.getPhotoReferences().size() + restaurant.getPhotoReferences() + " ");

             */
        }
    }

}

