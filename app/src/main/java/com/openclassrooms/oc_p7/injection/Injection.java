package com.openclassrooms.oc_p7.injection;

import com.openclassrooms.oc_p7.model.User;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.service.DummyWorkmateGenerator;
import com.openclassrooms.oc_p7.view.home.map.MapViewModelFactory;

import java.util.List;

public class Injection {

    public static PlaceRepository providePlaceRepository() {
        return new PlaceRepository();
    }

    public static MapViewModelFactory provideMapViewModelFactory() {
        PlaceRepository placeRepository = providePlaceRepository();
        return new MapViewModelFactory(placeRepository);
    }

    public static List<User> getWorkmates() {
        return DummyWorkmateGenerator.generateWorkmates();

    }
}
