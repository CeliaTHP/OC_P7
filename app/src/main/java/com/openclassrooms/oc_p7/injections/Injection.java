package com.openclassrooms.oc_p7.injections;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.services.apis.PlacesApi;
import com.openclassrooms.oc_p7.services.factories.DetailViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.MapViewModelFactory;
import com.openclassrooms.oc_p7.services.factories.WorkmateViewModelFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Injection {

    public static PlaceRepository providePlaceRepository() {
        return new PlaceRepository(Injection.provideApiClient());
    }

    public static WorkmateRepository provideWorkmateRepository(Context context) {
        return new WorkmateRepository();
    }

    public static MapViewModelFactory provideMapViewModelFactory(Context context, LifecycleOwner lifecycleOwner) {
        PlaceRepository placeRepository = providePlaceRepository();
        WorkmateRepository workmateRepository = provideWorkmateRepository(context);
        return new MapViewModelFactory(placeRepository, workmateRepository, Injection.provideFusedLocationProviderClient(context), lifecycleOwner);
    }

    public static WorkmateViewModelFactory provideWorkmateViewModelFactory(Context context) {
        WorkmateRepository workmateRepository = provideWorkmateRepository(context);
        return new WorkmateViewModelFactory(workmateRepository);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context) {
        WorkmateRepository workmateRepository = provideWorkmateRepository(context);
        PlaceRepository placeRepository = providePlaceRepository();
        return new DetailViewModelFactory(workmateRepository, placeRepository);
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
