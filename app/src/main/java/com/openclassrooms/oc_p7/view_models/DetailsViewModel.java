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

    public MutableLiveData<List<Workmate>> workmateListLiveData;

    public MutableLiveData<List<Workmate>> workmateForRestaurantListLiveData;


    public DetailsViewModel(WorkmateRepository workmateRepository, PlaceRepository placeRepository) {
        this.workmateRepository = workmateRepository;
        this.placeRepository = placeRepository;
        workmateForRestaurantListLiveData = workmateRepository.workmateForRestaurantListLiveData;
        workmateListLiveData = workmateRepository.workmateListLiveData;
    }

    public void getWorkmateList() {
        workmateRepository.getWorkmateList();
    }

    public void getWorkmatesForRestaurant(List<Workmate> workmateList, Restaurant restaurant) {
        workmateRepository.getWorkmatesForRestaurant(workmateList, restaurant);
    }

    public void getRestaurantDetails(Restaurant restaurant, OnSuccessListener onSuccessListener) {
        placeRepository.getRestaurantDetails(restaurant, onSuccessListener);
    }


}