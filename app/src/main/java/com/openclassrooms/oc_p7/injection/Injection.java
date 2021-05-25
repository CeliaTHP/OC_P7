package com.openclassrooms.oc_p7.injection;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.openclassrooms.oc_p7.model.User;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.service.DummyWorkmateGenerator;
import com.openclassrooms.oc_p7.view.home.map.MapViewModelFactory;

import java.util.List;

public class Injection {

    public static PlaceRepository providePlaceRepository(Context context) {
        return new PlaceRepository(provideFusedLocationProviderClient(context));
    }

    public static MapViewModelFactory provideMapViewModelFactory(Context context) {
        PlaceRepository placeRepository = providePlaceRepository(context);
        return new MapViewModelFactory(placeRepository);
    }

    public static List<User> getWorkmates() {
        return DummyWorkmateGenerator.generateWorkmates();
    }

    public static FusedLocationProviderClient provideFusedLocationProviderClient(Context context) {
        return LocationServices.getFusedLocationProviderClient(context);
    }

}
