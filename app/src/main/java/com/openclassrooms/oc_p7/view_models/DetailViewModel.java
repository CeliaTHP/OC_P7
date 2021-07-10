package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

import java.util.List;

public class DetailViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "DetailsViewModel";

    private WorkmateRepository workmateRepository;

    public MutableLiveData<List<Workmate>> workmateListLiveData;

    public MutableLiveData<List<Workmate>> workmateForRestaurantListLiveData;


    public DetailViewModel(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;
        workmateForRestaurantListLiveData = workmateRepository.workmateForRestaurantListLiveData;
        workmateListLiveData = workmateRepository.workmateListLiveData;
    }

    public void getWorkmateList() {
        workmateRepository.getWorkmateList();
    }

    public void getWorkmatesForRestaurant(List<Workmate> workmateList, Restaurant restaurant) {
        workmateRepository.getWorkmatesForRestaurant(workmateList, restaurant);
    }


}