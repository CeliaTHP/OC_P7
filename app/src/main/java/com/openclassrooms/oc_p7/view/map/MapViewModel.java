package com.openclassrooms.oc_p7.view.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.openclassrooms.oc_p7.model.Restaurant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModel";

    private MutableLiveData<String> mText;


    public MutableLiveData<Location> currentLocation = new MutableLiveData<>();
    public MutableLiveData<GoogleMap> mapLiveData = new MutableLiveData<>();


    public MapViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is map fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }


    public void initPlaces(GoogleMap googleMap, Context context) {

        List<Restaurant> restaurants = Collections.emptyList();

        Places.initialize(context, "AIzaSyAfA5LPuDm4ZaZzcifGry_RhEPLjmSi5N4");
        PlacesClient placesClient = Places.createClient(context);

        // Use fields to define the data types to return.
        List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.RATING);

        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    FindCurrentPlaceResponse responseLatLng = task.getResult();
                    for (PlaceLikelihood placeLikelihood : responseLatLng.getPlaceLikelihoods()) {

                        Log.d(TAG, placeLikelihood.toString());
                        googleMap.addMarker(new MarkerOptions().position(placeLikelihood.getPlace().getLatLng()).title(placeLikelihood.getPlace().getName()));

                    }
                        //AFTER GETTING ALL PLACES ADD USER POSITION
                        LatLng currentLatLng = new LatLng(currentLocation.getValue().getLatitude(), currentLocation.getValue().getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(currentLatLng).title("YOU").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15));

                    } else{
                        Exception exception = task.getException();
                        if (exception instanceof ApiException) {
                            ApiException apiException = (ApiException) exception;
                            Log.d(TAG, "PLACE NOT FOUND : " + apiException.getMessage());
                        }
                    }

                });
            } else{
                //ask permissions
            }
        }



    }

