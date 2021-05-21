package com.openclassrooms.oc_p7.view.home.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.oc_p7.repositories.PlaceRepository;

public class MapViewModelFactory implements ViewModelProvider.Factory {

    private final PlaceRepository placeRepository;

    public MapViewModelFactory(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(placeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
