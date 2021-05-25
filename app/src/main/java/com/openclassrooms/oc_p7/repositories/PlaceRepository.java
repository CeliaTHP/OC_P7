package com.openclassrooms.oc_p7.repositories;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.oc_p7.BuildConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceRepository {

    FusedLocationProviderClient fusedLocationProviderClient;

    public PlaceRepository(FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    private String TAG = "PlaceRepository";

    private ArrayList<Place> placeList = new ArrayList<>();

    public MutableLiveData<List<Place>> placesLiveData = new MutableLiveData<>();
    public MutableLiveData<Location> currentLocationLiveData = new MutableLiveData<>();

    @SuppressLint("MissingPermission") //Already asked for Location
    public void updateCurrentLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(location -> {
            if (location != null) {
                currentLocationLiveData.postValue(location);
            }
        });
    }

    //LIVEDATA

    public void getNearbyPlaces(Context context) {

        Places.initialize(context, BuildConfig.GoogleMapApiKey);
        PlacesClient placesClient = Places.createClient(context);

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.RATING, Place.Field.TYPES);

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse responseLatLng = task.getResult();
                    for (PlaceLikelihood placeLikelihood : responseLatLng.getPlaceLikelihoods()) {
                        Log.d(TAG, placeLikelihood.toString());
                        placeList.add(placeLikelihood.getPlace());
                        //googleMap.addMarker(new MarkerOptions().position(placeLikelihood.getPlace().getLatLng()).title(placeLikelihood.getPlace().getName()));

                    }
                    //LIST OF ALL PLACES NEAR USER
                    placesLiveData.postValue(placeList);
                    Log.d(TAG, placeList.toString());

                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.d(TAG, "PLACE NOT FOUND : " + apiException.getMessage());
                    }
                }


            });
        } else {
            //ask permissions
        }
    }

}
