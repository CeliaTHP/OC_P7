package com.openclassrooms.oc_p7.view.home.map;

import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.model.pojo_models.RestaurantResult;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private PlaceRepository placeRepository;

    public MutableLiveData<List<RestaurantResult>> nearbyPlacesLiveData;
    public MutableLiveData<Location> currentLocationLiveData;

    public MapViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        nearbyPlacesLiveData = placeRepository.nearbyPlacesLiveData;
        currentLocationLiveData = placeRepository.currentLocationLiveData;
    }

    public void getLocationInformations() {
        placeRepository.updateCurrentLocation();
    }

}

