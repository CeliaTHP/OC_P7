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
import com.google.android.gms.tasks.Task;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

import java.util.ArrayList;
import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private PlaceRepository placeRepository;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LifecycleOwner lifecycleOwner;
    private WorkmateRepository workmateRepository;
    private List<Restaurant> originalList = new ArrayList<>();
    private List<Restaurant> filteredRestaurantList = new ArrayList<>();
    private String query;


    public MutableLiveData<List<Restaurant>> restaurantListLiveData = new MutableLiveData<>();
    public MutableLiveData<Location> currentLocationLiveData;
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
        Log.d(TAG, "loadMap");

        placeRepository.getErrorCode().observe(this.lifecycleOwner, errorCode -> {
            Log.d(TAG, errorCode.toString());
            placeRepositoryErrorCodeMutableLiveData.postValue(errorCode);

        });

        workmateRepository.getErrorCode().observe(this.lifecycleOwner, errorCode -> {
            workmateRepositoryErrorCodeMutableLiveData.postValue(errorCode);
        });

        placeRepository.getRestaurantListMutableLiveData().observe(this.lifecycleOwner, restaurantList -> {
            Log.d(TAG, " getRestaurantLiveData observer ");
            // filterList();
            restaurantListLiveData.postValue(restaurantList);


        });


    }

    private void filterList() {
        originalList = placeRepository.getRestaurantListMutableLiveData().getValue();
        filteredRestaurantList.clear();
        if (originalList != null) {
            if (query != null) {
                for (Restaurant restaurant : originalList) {
                    if (restaurant.getName().toLowerCase().contains(query.toLowerCase())) {
                        filteredRestaurantList.add(restaurant);
                    }
                }
                restaurantListLiveData.postValue(filteredRestaurantList);
            } else {
                restaurantListLiveData.postValue(originalList);
            }
        }
    }

    public void filterList(String query) {
        this.query = query;
        filterList();
    }


    public void updateRestaurantDetails(Restaurant restaurant) {
        placeRepository.updateRestaurantDetails(restaurant.getId());
    }

    public void getLocationInformations(Context context) {
        loadMap();
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


}

