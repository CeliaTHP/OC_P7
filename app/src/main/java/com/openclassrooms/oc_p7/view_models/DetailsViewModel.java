package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

public class DetailsViewModel extends ViewModel {

    private static final String TAG = "DetailsViewModel";

    private final WorkmateRepository workmateRepository;
    private final PlaceRepository placeRepository;
    public MutableLiveData<Restaurant> restaurantMutableLiveData;


    public DetailsViewModel(WorkmateRepository workmateRepository, PlaceRepository placeRepository) {
        this.workmateRepository = workmateRepository;
        this.placeRepository = placeRepository;
        this.restaurantMutableLiveData = placeRepository.restaurantMutableLiveData;

    }

    public void getWorkmatesForRestaurant() {
        workmateRepository.getWorkmatesForRestaurant(restaurantMutableLiveData);
    }

    public void getRestaurantDetails(String restaurantId) {
        placeRepository.getRestaurantDetails(restaurantId);
    }


}