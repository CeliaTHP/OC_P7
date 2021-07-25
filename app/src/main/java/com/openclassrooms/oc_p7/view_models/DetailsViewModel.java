package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnSuccessListener;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

import java.util.List;

public class DetailsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "DetailsViewModel";

    private WorkmateRepository workmateRepository;
    private PlaceRepository placeRepository;

    public MutableLiveData<List<Workmate>> workmateForRestaurantListLiveData;


    public DetailsViewModel(WorkmateRepository workmateRepository, PlaceRepository placeRepository) {
        this.workmateRepository = workmateRepository;
        this.placeRepository = placeRepository;
        workmateForRestaurantListLiveData = workmateRepository.workmateForRestaurantListLiveData;
    }

    public void getWorkmatesForRestaurant(Restaurant restaurant) {
        workmateRepository.getWorkmatesForRestaurant(restaurant);
    }

    public void getRestaurantDetails(String restaurantId, OnSuccessListener onSuccessListener) {
        placeRepository.getRestaurantDetails(new Restaurant(restaurantId, null, null, 0.0, 0.0), onSuccessListener);
    }


}