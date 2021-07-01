package com.openclassrooms.oc_p7.view_models;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private PlaceRepository placeRepository;
    private FusedLocationProviderClient fusedLocationProviderClient;


    public MutableLiveData<List<Restaurant>> restaurantLiveData;
   // public MutableLiveData<List<RestaurantPojo>> nearbyPlacesLiveData;
    public MutableLiveData<Location> currentLocationLiveData;

    public MapViewModel(PlaceRepository placeRepository, FusedLocationProviderClient fusedLocationProviderClient) {
        this.placeRepository = placeRepository;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        //nearbyPlacesLiveData = placeRepository.nearbyPlacesLiveData;
        currentLocationLiveData = placeRepository.currentLocationLiveData;
        restaurantLiveData = placeRepository.restaurantLiveData;
    }

    public void getLocationInformations(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

    public void getRestaurantDetails(Restaurant restaurant) {
        placeRepository.getRestaurantDetails(restaurant);

    }


}

