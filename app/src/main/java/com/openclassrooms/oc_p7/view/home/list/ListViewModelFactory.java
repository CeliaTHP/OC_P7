package com.openclassrooms.oc_p7.view.home.list;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.oc_p7.repositories.PlaceRepository;

public class ListViewModelFactory implements ViewModelProvider.Factory {

    private final PlaceRepository placeRepository;

    public ListViewModelFactory(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(placeRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
