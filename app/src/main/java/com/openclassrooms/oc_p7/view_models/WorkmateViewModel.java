package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

import java.util.List;

public class WorkmateViewModel extends ViewModel {


    private final WorkmateRepository workmateRepository;

    public MutableLiveData<List<Workmate>> workmateListLiveData;


    public WorkmateViewModel(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;
        workmateListLiveData = workmateRepository.getWorkmateListLiveData();
    }

    public void getWorkmateList() {
        workmateRepository.getWorkmateList();
    }

    public void getWorkmateForRestaurantList(MutableLiveData<List<Restaurant>> restaurantMutableLiveData) {
        workmateRepository.getWorkmatesForRestaurantList(restaurantMutableLiveData);
    }


}