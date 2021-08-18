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

    public void setWorkmatesForRestaurant(Restaurant restaurant) {
        workmateRepository.setWorkmatesForRestaurant(restaurant);
    }

    public void setRestaurantDetails(Restaurant restaurant, OnSuccessListener<Restaurant> onSuccessListener) {
        placeRepository.setRestaurantDetails(restaurant, onSuccessListener);
    }


}