package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "DetailsViewModel";

    private WorkmateRepository workmateRepository;
    private PlaceRepository placeRepository;
    public MutableLiveData<Restaurant> restaurantMutableLiveData;


    public DetailsViewModel(WorkmateRepository workmateRepository, PlaceRepository placeRepository) {
        this.workmateRepository = workmateRepository;
        this.placeRepository = placeRepository;
        this.restaurantMutableLiveData = placeRepository.restaurantMutableLiveData;

    }

    public void getWorkmatesForRestaurant(Restaurant restaurant) {
        workmateRepository.getWorkmatesForRestaurant(restaurant);
    }

    public void getRestaurantDetails(String restaurantId) {
        placeRepository.getRestaurantDetails(restaurantId);
    }


}