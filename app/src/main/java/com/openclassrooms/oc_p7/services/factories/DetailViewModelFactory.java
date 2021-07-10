package com.openclassrooms.oc_p7.services.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.DetailsViewModel;

public class DetailViewModelFactory implements ViewModelProvider.Factory {

    private final WorkmateRepository workmateRepository;
    private final PlaceRepository placeRepository;

    public DetailViewModelFactory(WorkmateRepository workmateRepository, PlaceRepository placeRepository) {
        this.workmateRepository = workmateRepository;
        this.placeRepository = placeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailsViewModel.class)) {
            return (T) new DetailsViewModel(workmateRepository, placeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
