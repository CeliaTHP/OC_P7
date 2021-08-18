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
import com.openclassrooms.oc_p7.services.utils.RestaurantConverter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {

    private PlacesApi placesApi;


    public PlaceRepository(PlacesApi placesApi) {
        this.placesApi = placesApi;
    }


    private String TAG = "PlaceRepository";


    private ArrayList<RestaurantPojo> placeList = new ArrayList<>();
    private ArrayList<Restaurant> restaurantList = new ArrayList<>();

    private ArrayList<RestaurantPojo> newPlaceList = new ArrayList<>();

    private MutableLiveData<List<Restaurant>> newRestaurantLiveData = new MutableLiveData<>();


    private MutableLiveData<List<Restaurant>> restaurantLiveData = new MutableLiveData<>();

    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    private int count = 0;


    public LiveData<List<Restaurant>> getRestaurantLiveData() {
        return restaurantLiveData;
    }

    public LiveData<List<Restaurant>> getNewRestaurantLiveData() {
        return newRestaurantLiveData;
    }


    public void getPlacesAsRestaurants(Location location) {
        Log.d("NEW_REPO", "getPlacesAsRestaurants ");
        String radiusQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_radius);
        String restaurantQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_restaurant);

        String locationStringQuery = location.getLatitude() + "," + location.getLongitude();

        Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(BuildConfig.GoogleMapApiKey, locationStringQuery, radiusQuery, restaurantQuery);
        call.enqueue(new Callback<NearbyPlaceResponse>() {

            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                newPlaceList.clear();
                newPlaceList.addAll(response.body().restaurantPojos);
                newRestaurantLiveData.postValue(RestaurantConverter.transformToRestaurants(newPlaceList, location));

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {

            }

        });

    }

    public void getNearbyPlaces(Location location) {
        String radiusQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_radius);
        String restaurantQuery = MyApplication.getInstance().getApplicationContext().getString(R.string.query_restaurant);

        String locationStringQuery = location.getLatitude() + "," + location.getLongitude();

        Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(BuildConfig.GoogleMapApiKey, locationStringQuery, radiusQuery, restaurantQuery);
        call.enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                placeList.addAll(response.body().restaurantPojos);
                for (RestaurantPojo restaurantPojo : placeList) {
                    Restaurant restaurant = createRestaurant(restaurantPojo);
                    restaurantList.add(restaurant);
                }
                Log.d(TAG, "getNearbyPlaces restaurantList =  " + restaurantList);

                restaurantLiveData.postValue(restaurantList);

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

    public void setRestaurantDetails(Restaurant restaurant, OnSuccessListener<Restaurant> onSuccessListener) {
        placesApi.getDetailsById(BuildConfig.GoogleMapApiKey, restaurant.getId()).enqueue(new Callback<DetailsPlaceResponse>() {

            @Override
            public void onResponse(Call<DetailsPlaceResponse> call, Response<DetailsPlaceResponse> response) {
                setRestaurantInfos(response.body().result, restaurant);
                if (onSuccessListener != null)
                    onSuccessListener.onSuccess(restaurant);
                Log.d("NEW_REPO_RESPONSE", restaurant.toString());
            }

            @Override
            public void onFailure(Call<DetailsPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });
        Log.d("NEW_REPO_AFTER_SET", restaurant.toString());
    }


/*
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

            }

        });

    }



 */

    private Restaurant createRestaurant(RestaurantPojo restaurantPojo) {
        return new Restaurant(restaurantPojo.place_id, restaurantPojo.name, restaurantPojo.vicinity, restaurantPojo.geometry.location.lat, restaurantPojo.geometry.location.lng);
    }

    private void setRestaurantInfos(RestaurantDetailsPojo restaurantDetailsPojo, Restaurant restaurant) {

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

            Log.d("NEW_REPO_AFTER", restaurant.toString());

        }
    }

}

