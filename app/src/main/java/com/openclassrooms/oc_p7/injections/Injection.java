package com.openclassrooms.oc_p7.injections;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.openclassrooms.oc_p7.databases.RestaurantDao;
import com.openclassrooms.oc_p7.databases.RestaurantDatabase;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import com.openclassrooms.oc_p7.services.factories.ListViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    public static PlaceRepository providePlaceRepository(Context context) {
        return new PlaceRepository(provideFusedLocationProviderClient(context), provideRestaurantDao(context));
    }

    public static MapViewModelFactory provideMapViewModelFactory(Context context) {
        PlaceRepository placeRepository = providePlaceRepository(context);
        return new MapViewModelFactory(placeRepository);
    }

    public static ListViewModelFactory provideListViewModelFactory(Context context) {
        PlaceRepository placeRepository = providePlaceRepository(context);
        return new ListViewModelFactory(placeRepository);
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

    public static RestaurantDao provideRestaurantDao(Context context) {
        return RestaurantDatabase.getInstance(context).getRestaurantDao();
    }


}
