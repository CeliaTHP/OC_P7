package com.openclassrooms.oc_p7.view.home.map;

import android.app.Activity;
import android.location.Location;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private PlaceRepository placeRepository;

    public MutableLiveData<List<Place>> placeListLiveData;
    public MutableLiveData<Location> currentLocationLiveData;

    public MapViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        placeListLiveData = placeRepository.placesLiveData;
        currentLocationLiveData = placeRepository.currentLocationLiveData;
    }

    //PASSER MAP A CHAQUE FOIS OU RECUP VALEURS ET UPDATE AVEC LA MAP DEPUIS VM ?
    //AJOUT DES MARQEURS ICI ???
    // REPO DOIVENT UNIQUEMENT RETOURNER LES DONNEES ?

    public void getNearbyPlaces(Activity activity) {
        //NO MAP FOR REPOSITORY
        placeRepository.getNearbyPlaces(activity);
    }

    public void getCurrentLocation() {
        placeRepository.updateCurrentLocation();
    }
}

