package com.openclassrooms.oc_p7.view_models;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.repositories.WorkmateRepository;

import java.util.List;

public class WorkmateViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "WorkmateListViewModel";

    private WorkmateRepository workmateRepository;


    public MutableLiveData<List<Workmate>> workmateListLiveData;
    public MutableLiveData<List<String>> workmatePlaceIdListLiveData;


    public WorkmateViewModel(WorkmateRepository workmateRepository) {
        this.workmateRepository = workmateRepository;
        workmateListLiveData = workmateRepository.workmateListLiveData;
        workmatePlaceIdListLiveData = workmateRepository.workmatePlaceIdListLiveData;

    }

    public void getWorkmateList() {
        workmateRepository.getWorkmateList();
    }


}