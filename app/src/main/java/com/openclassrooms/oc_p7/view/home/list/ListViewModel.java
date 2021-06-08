package com.openclassrooms.oc_p7.view.home.list;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.model.Restaurant;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

import java.util.List;

public class ListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private PlaceRepository placeRepository;

    public ListViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public List<Restaurant> getAllRestaurant() {
        return placeRepository.getNearbyPlacesFromDatabase();
    }

    public void getDetailsById() {
        placeRepository.getDetailsById();
    }


}