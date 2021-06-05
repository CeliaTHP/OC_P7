package com.openclassrooms.oc_p7.repositories;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.database.RestaurantDao;
import com.openclassrooms.oc_p7.injection.Injection;
import com.openclassrooms.oc_p7.model.Restaurant;
import com.openclassrooms.oc_p7.model.pojo_models.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.model.pojo_models.RestaurantResult;
import com.openclassrooms.oc_p7.service.api.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceRepository {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private RestaurantDao restaurantDao;

    public PlaceRepository(FusedLocationProviderClient fusedLocationProviderClient, RestaurantDao restaurantDao) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.restaurantDao = restaurantDao;
    }

    private String TAG = "PlaceRepository";

    private ArrayList<RestaurantResult> placeList = new ArrayList<>();

    public MutableLiveData<List<RestaurantResult>> nearbyPlacesLiveData = new MutableLiveData<>();
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

    public List<Restaurant> getNearbyPlacesFromDatabase() {
        return restaurantDao.getAll();
    }

    public void getNearbyPlaces(Location location) {
        //working with fakeLocation !!!!
        PlacesApi placesApi = Injection.provideApiClient();

        String fakeLocation = "49.024979226793775,2.463881854135891";
//        String location = currentLocationLiveData.getValue().getLatitude() + "," + currentLocationLiveData.getValue().getLongitude();
        String radius = "100";

        Log.d(TAG, "expected format : " + fakeLocation);
        Log.d(TAG, "format is : " + location);

        String locationString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
        Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(locationString, BuildConfig.GoogleMapApiKey, radius, "restaurant");
        call.enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                for (RestaurantResult restaurantResult : response.body().restaurantResults) {
                    placeList.add(restaurantResult);
                    restaurantDao.createRestaurant(new Restaurant(restaurantResult.place_id, restaurantResult.name, restaurantResult.geometry.location.lat,
                            restaurantResult.geometry.location.lng, restaurantResult.rating, false, false));


                    Log.d(TAG, "name: " + restaurantResult.name);
                    Log.d(TAG, "lat " + restaurantResult.geometry.location.lat);

                    if (restaurantResult.opening_hours != null)
                        Log.d(TAG, "open now" + restaurantResult.opening_hours.open_now);
                    Log.d(TAG, "types " + restaurantResult.types);

                }
                nearbyPlacesLiveData.postValue(placeList);

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


    }
}
