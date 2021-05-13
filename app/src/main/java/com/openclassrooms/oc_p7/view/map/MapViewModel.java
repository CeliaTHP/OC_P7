package com.openclassrooms.oc_p7.view.map;

import android.app.Activity;
import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.libraries.places.api.model.Place;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";
    private PlaceRepository placeRepository;
    private MutableLiveData<String> mText;


    public MutableLiveData<GoogleMap> mapLiveData = new MutableLiveData<>();

    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();
    public MutableLiveData<List<Place>> placeListLiveData;


    public MapViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        placeListLiveData = placeRepository.placesLiveData;
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    //PASSER MAP A CHAQUE FOIS OU RECUP VALEURS ET UPDATE AVEC LA MAP DEPUIS VM ?
    //AJOUT DES MARQEURS ICI ???
    // REPO DOIVENT UNIQUEMENT RETOURNER LES DONNEES ?

    public void getNearbyPlaces(Activity activity) {
        //NO MAP FOR REPOSITORY
        placeRepository.getNearbyPlaces(activity);
    }

    public void setCurrentLocation(Location location) {
        currentLocationLiveData.postValue(location);
    }

    public void updateCurrentLocation(Location location) {
        placeRepository.updateCurrentLocation(location, mapLiveData.getValue());

    }


}

