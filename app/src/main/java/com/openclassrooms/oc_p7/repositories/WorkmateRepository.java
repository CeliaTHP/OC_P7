package com.openclassrooms.oc_p7.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;

import java.util.ArrayList;
import java.util.List;

public class WorkmateRepository {

    private String TAG = "WorkmateRepository";

    public MutableLiveData<List<Workmate>> workmateListLiveData = new MutableLiveData<>();
    public MutableLiveData<List<String>> workmatePlaceIdListLiveData = new MutableLiveData<>();

    private ArrayList<String> workmatePlaceIdList = new ArrayList<>();

    private ArrayList<Workmate> workmateList = new ArrayList<>();


    public void getWorkmateList() {

        WorkmateHelper.getWorkmatesCollection().get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    Log.d(TAG, "onSuccess");
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot snapshot : snapshotList) {
                        Workmate workmate = new Workmate(
                                snapshot.get("uid").toString(),
                                snapshot.get("name").toString(),
                                snapshot.get("email").toString(),
                                snapshot.get("picUrl").toString()
                        );

                        if (snapshot.get("restaurantName") != null && snapshot.get("restaurantType") != null && snapshot.get("restaurantId") != null) {
                            workmate.setRestaurantName(snapshot.get("restaurantName").toString());
                            workmate.setRestaurantType(snapshot.get("restaurantType").toString());
                            workmate.setRestaurantId(snapshot.get("restaurantId").toString());
                            workmatePlaceIdList.add(workmate.getRestaurantId());
                            Log.d(TAG, "restaurant : " + workmate.getRestaurantId());
                        }
                        workmateList.add(workmate);
                    }
                    Log.d(TAG, "workmateList  : " + workmateList + "");
                    Log.d(TAG, "workmatePlaceIdList  : " + workmatePlaceIdList + "");

                    workmateListLiveData.postValue(workmateList);
                    workmatePlaceIdListLiveData.postValue(workmatePlaceIdList);
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure"));
    }


}
