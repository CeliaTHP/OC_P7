package com.openclassrooms.oc_p7.services.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.DetailViewModel;

public class DetailViewModelFactory implements ViewModelProvider.Factory {

    private final WorkmateRepository workmateRepository;

    public DetailViewModelFactory(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DetailViewModel.class)) {
            return (T) new DetailViewModel(workmateRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
