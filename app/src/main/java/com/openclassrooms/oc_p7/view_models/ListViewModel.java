package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.openclassrooms.oc_p7.repositories.PlaceRepository;

public class ListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private PlaceRepository placeRepository;

    private Task mTask;
    private final static String TAG = "ListViewModel";


    public ListViewModel(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    public void getDetailsById() {
        placeRepository.getDetailsById();
    }


}