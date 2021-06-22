package com.openclassrooms.oc_p7.services.factories;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.oc_p7.repositories.WorkmateRepository;
import com.openclassrooms.oc_p7.view_models.WorkmateViewModel;

public class WorkmateViewModelFactory implements ViewModelProvider.Factory {

    private final WorkmateRepository workmateRepository;

    public WorkmateViewModelFactory(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel(workmateRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
