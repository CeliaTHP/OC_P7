package com.openclassrooms.oc_p7.view.map;

import android.app.Activity;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private PlaceRepository placeRepository;
    private MutableLiveData<String> mText;


    public MutableLiveData<GoogleMap> mapLiveData = new MutableLiveData<>();
    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();


    public MapViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    //PASSER MAP A CHAQUE FOIS OU RECUP VALEURS ET UPDATE AVEC LA MAP DEPUIS VM ?
    //AJOUT DES MARQEURS ICI ???
    // REPO DOIVENT UNIQUEMENT RETOURNER LES DONNEES ?

    public void getNearbyPlaces(GoogleMap googleMap, Activity activity) {
        placeRepository.getNearbyPlaces(googleMap, activity);
    }

    public void updateCurrentLocation(Location location) {
        placeRepository.updateCurrentLocation(location, mapLiveData.getValue());

    }


}

