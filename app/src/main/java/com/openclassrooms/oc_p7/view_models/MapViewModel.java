package com.openclassrooms.oc_p7.view_models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private final PlaceRepository placeRepository;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LifecycleOwner lifecycleOwner;
    private final WorkmateRepository workmateRepository;

    public MutableLiveData<Location> currentLocationLiveData;

    public MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Restaurant>> requestedRestaurantList = new MutableLiveData<>();
    public MutableLiveData<ErrorCode> placeRepositoryErrorCodeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ErrorCode> workmateRepositoryErrorCodeMutableLiveData = new MutableLiveData<>();


    public MapViewModel(PlaceRepository placeRepository, WorkmateRepository workmateRepository, FusedLocationProviderClient fusedLocationProviderClient, LifecycleOwner lifecycleOwner) {
        this.placeRepository = placeRepository;
        this.workmateRepository = workmateRepository;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.lifecycleOwner = lifecycleOwner;
        this.currentLocationLiveData = placeRepository.currentLocationLiveData;

    }


    public void loadMap() {
        placeRepository.getErrorCode().observe(this.lifecycleOwner, errorCode -> {
            placeRepositoryErrorCodeMutableLiveData.postValue(errorCode);
        });

        workmateRepository.getErrorCode().observe(this.lifecycleOwner, errorCode -> {
            workmateRepositoryErrorCodeMutableLiveData.postValue(errorCode);
        });

        placeRepository.getRestaurantListMutableLiveData().observe(this.lifecycleOwner, restaurantList -> {
            restaurantListLiveData.postValue(restaurantList);
        });

        placeRepository.getRequestedRestaurantListMutableLiveData().observe(this.lifecycleOwner, restaurantList -> {
            for (Restaurant restaurant : restaurantList) {
                Log.d(TAG, restaurant.toString());
                if (!restaurant.getHasDetails()) {
                    updateRequestedRestaurantDetails(restaurant);

                }
            }
            requestedRestaurantList.postValue(restaurantList);
        });

    }


    public void updateRestaurantDetails(Restaurant restaurant) {
        placeRepository.updateRestaurantDetails(restaurant.getId());
    }

    public void updateRequestedRestaurantDetails(Restaurant restaurant) {
        placeRepository.updateRequestedRestaurantDetails(restaurant.getId());
    }

    public void getRequestedRestaurants(String request, LatLng latLng) {
        placeRepository.getRequestedRestaurants(request, latLng);
    }

    public void getLocationInformations(Context context) {
        loadMap();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                Log.d(TAG, location + "");
                currentLocationLiveData.postValue(location);
                placeRepository.getNearbyPlaces(location);
            }
        });

    }


}

