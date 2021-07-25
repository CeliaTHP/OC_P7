package com.openclassrooms.oc_p7.services.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.view_models.MapViewModel;

public class MapViewModelFactory implements ViewModelProvider.Factory {

    private final PlaceRepository placeRepository;
    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LifecycleOwner lifecycleOwner;

    public MapViewModelFactory(PlaceRepository placeRepository, FusedLocationProviderClient fusedLocationProviderClient, LifecycleOwner lifecycleOwner) {
        this.placeRepository = placeRepository;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MapViewModel.class)) {
            return (T) new MapViewModel(placeRepository, fusedLocationProviderClient, lifecycleOwner);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
