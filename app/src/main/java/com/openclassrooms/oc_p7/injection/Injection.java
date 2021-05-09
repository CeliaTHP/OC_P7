package com.openclassrooms.oc_p7.injection;

import android.content.Context;

import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.view.map.MapViewModelFactory;

public class Injection {

    public static PlaceRepository providePlaceRepository() {
        return new PlaceRepository();
    }

    public static MapViewModelFactory provideMapViewModelFactory(Context context) {
        PlaceRepository placeRepository = providePlaceRepository();
        return new MapViewModelFactory(placeRepository);
    }
}
