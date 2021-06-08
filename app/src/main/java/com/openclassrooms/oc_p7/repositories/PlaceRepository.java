package com.openclassrooms.oc_p7.repositories;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.MyApplication;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.database.RestaurantDao;
import com.openclassrooms.oc_p7.injection.Injection;
import com.openclassrooms.oc_p7.model.pojo_models.details.DetailPlaceResponse;
import com.openclassrooms.oc_p7.model.pojo_models.general.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.model.pojo_models.general.Restaurant;
import com.openclassrooms.oc_p7.service.api.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {

    private PlacesApi placesApi = Injection.provideApiClient();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private RestaurantDao restaurantDao;

    public PlaceRepository(FusedLocationProviderClient fusedLocationProviderClient, RestaurantDao restaurantDao) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.restaurantDao = restaurantDao;
    }

    private String TAG = "PlaceRepository";

    private ArrayList<Restaurant> placeList = new ArrayList<>();

    public MutableLiveData<List<Restaurant>> nearbyPlacesLiveData = new MutableLiveData<>();
    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();

    @SuppressLint("MissingPermission") //Already asked for Location
    public void updateCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocationLiveData.postValue(location);
                getNearbyPlaces(location);
            }
        });
    }

    public List<com.openclassrooms.oc_p7.model.Restaurant> getNearbyPlacesFromDatabase() {
        return restaurantDao.getAll();
    }

    public void getNearbyPlaces(Location location) {
        //working with fakeLocation !!!!

        String fakeLocation = "49.024979226793775,2.463881854135891";
//        String location = currentLocationLiveData.getValue().getLatitude() + "," + currentLocationLiveData.getValue().getLongitude();
        Log.d(TAG, "expected format : " + fakeLocation);
        Log.d(TAG, "format is : " + location);

        String radius = MyApplication.getInstance().getApplicationContext().getString(R.string.query_radius);
        String restaurant = MyApplication.getInstance().getApplicationContext().getString(R.string.query_restaurant);

        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());

        Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(BuildConfig.GoogleMapApiKey, locationString, radius, restaurant);
        call.enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                for (Restaurant restaurant : response.body().restaurants) {
                    placeList.add(restaurant);

                    Log.d(TAG, "name : " + restaurant.name);
                    Log.d(TAG, "lat " + restaurant.geometry.location.lat);

                    if (restaurant.opening_hours != null)
                        Log.d(TAG, "open now " + restaurant.opening_hours.open_now);
                    Log.d(TAG, "types " + restaurant.types);

                }
                nearbyPlacesLiveData.postValue(placeList);

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });

    }

    public void getDetailsById() {
        placesApi.getDetailsById(BuildConfig.GoogleMapApiKey, "ChIJZRd-EQRA5kcRnyzZQ1QWzVw").enqueue(new Callback<DetailPlaceResponse>() {
            @Override
            public void onResponse(Call<DetailPlaceResponse> call, Response<DetailPlaceResponse> response) {
                Log.d(TAG, response.body().result.name);

            }

            @Override
            public void onFailure(Call<DetailPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());


            }
        });
    }


}
