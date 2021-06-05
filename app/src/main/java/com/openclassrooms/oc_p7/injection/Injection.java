package com.openclassrooms.oc_p7.injection;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.service.api.PlacesApi;
import com.openclassrooms.oc_p7.view.home.map.MapViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    public static PlaceRepository providePlaceRepository(Context context) {
        return new PlaceRepository(provideFusedLocationProviderClient(context));
    }

    public static MapViewModelFactory provideMapViewModelFactory(Context context) {
        PlaceRepository placeRepository = providePlaceRepository(context);
        return new MapViewModelFactory(placeRepository);
    }

    public static FusedLocationProviderClient provideFusedLocationProviderClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

    public static PlacesApi provideApiClient() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(PlacesApi.class);

    }

}
