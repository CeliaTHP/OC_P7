package com.openclassrooms.oc_p7.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorkmateRepository {

    private String TAG = "WorkmateRepository";

    public MutableLiveData<List<Workmate>> workmateListLiveData = new MutableLiveData<>();

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
                                //toObject instead ?
                        );

                        if (snapshot.get("restaurantName") != null && snapshot.get("restaurantId") != null) {
                            workmate.setRestaurantName(snapshot.get("restaurantName").toString());
                            workmate.setRestaurantId(snapshot.get("restaurantId").toString());
                            Log.d(TAG, "restaurant : " + workmate.getRestaurantId());
                        }
                        workmateList.add(workmate);
                    }
                    Log.d(TAG, "workmateList  : " + workmateList + "");

                    workmateListLiveData.postValue(workmateList);
                })
                .addOnFailureListener(e -> Log.d(TAG, "onFailure"));
    }

    public void getWorkmatesForRestaurant(Restaurant restaurant, OnSuccessListener<Restaurant> onSuccessListener) {
        //FILTER VIA FIREBASE
        List<Workmate> workmatesFiltered = new ArrayList<>();
        WorkmateHelper.getWorkmatesForRestaurant(restaurant.getId()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                        workmatesFiltered.add(workmateToAdd);
                        Log.d(TAG, "added : " + workmateToAdd.getName() + " to " + restaurant.getName());
                    }
                    //UPDATE RESTAURANTLIVEDATA
                    Log.d(TAG, "workmateForRestaurant  : " + restaurant.getName() + "=" + workmatesFiltered);

                    restaurant.setAttendees(workmatesFiltered);
                    onSuccessListener.onSuccess(restaurant);

                    Log.d(TAG, restaurant.toString());

                }
            }

        });

    }

    public void getWorkmatesForRestaurantsList(List<Restaurant> restaurantList, OnSuccessListener<List<Restaurant>> onSuccessListener) {
        //FILTER VIA FIREBASE
        for (Restaurant restaurant : restaurantList) {
            List<Workmate> workmatesFiltered = new ArrayList<>();
            WorkmateHelper.getWorkmatesForRestaurant(restaurant.getId()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                            workmatesFiltered.add(workmateToAdd);
                            Log.d(TAG, "withList added : " + workmateToAdd.getName() + " to " + restaurant.getName());
                        }
                        //UPDATE RESTAURANTLIVEDATA
                        Log.d(TAG, "withList workmateForRestaurant : " + workmatesFiltered);

                        restaurant.setAttendees(workmatesFiltered);
                        onSuccessListener.onSuccess(restaurantList);

                    }
                }

            });
        }
    }


/*
        for (Workmate workmate : workmateList) {
            if (workmate.getRestaurantId() != null) {
                if (workmate.getRestaurantId().equals(restaurant.getId())) {
                    workmateForRestaurantList.add(workmate);
                    Log.d(TAG, workmate.getName() + "matches with restaurant :  " + restaurant.getName());
                }
            }
        }
        workmateForRestaurantListLiveData.postValue(workmateForRestaurantList);
    }

 */
}
