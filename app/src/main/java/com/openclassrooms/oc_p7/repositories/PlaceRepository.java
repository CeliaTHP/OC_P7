package com.openclassrooms.oc_p7.repositories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.openclassrooms.oc_p7.BuildConfig;
import com.openclassrooms.oc_p7.injection.Injection;
import com.openclassrooms.oc_p7.model.pojo.NearbyPlaceResponse;
import com.openclassrooms.oc_p7.model.pojo.Result;
import com.openclassrooms.oc_p7.service.api.PlacesApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public void getNearbyPlaces(Context context) {
        PlacesApi placesApi = Injection.provideApiClient();
        String location = "49.024979226793775,2.463881854135891";
        String radius = "100";

        NearbyPlaceResponse nearbyPlaceResponse = new NearbyPlaceResponse();


        Call<NearbyPlaceResponse> call = placesApi.getNearbyPlaces(location, BuildConfig.GoogleMapApiKey, radius);
        call.enqueue(new Callback<NearbyPlaceResponse>() {
            @Override
            public void onResponse(Call<NearbyPlaceResponse> call, Response<NearbyPlaceResponse> response) {
                for (Result result : response.body().results) {
                    Log.d(TAG, "response : " + result.name);
                }

            }

            @Override
            public void onFailure(Call<NearbyPlaceResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());

            }
        });


        //     Log.d(TAG, "call " + call);
        //                Log.d(TAG, "response : " + response);
        //                Log.d(TAG, "onFailure: " + t.getMessage() );
    }


    //LIVEDATA
/*
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


 */
}
