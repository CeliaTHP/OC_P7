package com.openclassrooms.oc_p7.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class WorkmateRepository {

    private Executor executor;

    public WorkmateRepository(Executor executor) {
        this.executor = executor;

    }

    private String TAG = "WorkmateRepository";

    public MutableLiveData<List<Workmate>> workmateListLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> hasError = new MutableLiveData<>();

    private ArrayList<Workmate> workmateList = new ArrayList<>();


    public void getWorkmateList() {

        executor.execute(() -> {
            Task<QuerySnapshot> task = WorkmateHelper.getWorkmatesCollection().get();
            try {
                Tasks.await(task);
                QuerySnapshot queryDocumentSnapshots = task.getResult();

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
            } catch (ExecutionException e) {
                Log.d(TAG, "ExecutionException while getting workmateList : " + e.getLocalizedMessage());


            } catch (InterruptedException e) {
                Log.d(TAG, "InterruptedException while getting workmateList : " + e.getMessage());


            }
        });

    }

    public void getWorkmatesForRestaurant(Restaurant restaurant) {
        //FILTER VIA FIREBASE
        Log.d(TAG, "getWorkmatesForRestaurant");


        executor.execute(() -> {
            List<Workmate> workmatesFiltered = new ArrayList<>();
            Task<QuerySnapshot> task = WorkmateHelper.getWorkmatesForRestaurant(restaurant.getId());

            try {
                Tasks.await(task);
                QuerySnapshot queryDocumentSnapshots = task.getResult();
                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();

                for (DocumentSnapshot documentSnapshot : snapshotList) {
                    Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                    workmatesFiltered.add(workmateToAdd);
                    Log.d(TAG, "added : " + workmateToAdd.getName() + " to " + restaurant.getName());
                }
                //UPDATE RESTAURANTLIVEDATA

                restaurant.setAttendees(workmatesFiltered);

                Log.d(TAG, restaurant.toString());

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

    }


    public void getWorkmatesForRestaurantsList(MutableLiveData<List<Restaurant>> restaurantListMutableLiveData) {
        //FILTER VIA FIREBASE

        List<Restaurant> restaurantList = restaurantListMutableLiveData.getValue();
        executor.execute(() -> {

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

                            restaurant.setAttendees(workmatesFiltered);

                        }
                    }

                });
            }

        });
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
