package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "DetailsViewModel";

    private WorkmateRepository workmateRepository;
    private PlaceRepository placeRepository;


    public DetailsViewModel(WorkmateRepository workmateRepository, PlaceRepository placeRepository) {
        this.workmateRepository = workmateRepository;
        this.placeRepository = placeRepository;
    }

    public void getWorkmatesForRestaurant(Restaurant restaurant, OnSuccessListener onSuccessListener) {
        workmateRepository.getWorkmatesForRestaurant(restaurant, onSuccessListener);
    }

    public void getRestaurantDetails(String restaurantId, Restaurant restaurant, OnSuccessListener onSuccessListener) {
        placeRepository.getRestaurantDetails(restaurantId, restaurant, onSuccessListener);
    }


}