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


    public MapViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public void setCurrentLocation(Location location) {
        placeRepository.currentLocation.postValue(location);

    }

    public void initPlaces(GoogleMap googleMap, Activity activity) {
        placeRepository.initPlaces(googleMap, activity);
    }


}

