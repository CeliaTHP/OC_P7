package com.openclassrooms.oc_p7.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.ErrorCode;
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
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<ErrorCode> errorCode;

    private MutableLiveData<List<Workmate>> workmateListLiveData;
    private WorkmateHelper workmateHelper;

    public WorkmateRepository(FirebaseFirestore firebaseFirestore, WorkmateHelper workmateHelper, Executor executor, MutableLiveData<List<Workmate>> workmateListLiveData, MutableLiveData<ErrorCode> errorCode) {
        this.firebaseFirestore = firebaseFirestore;
        this.workmateHelper = workmateHelper;
        this.executor = executor;
        this.workmateListLiveData = workmateListLiveData;
        this.errorCode = errorCode;
    }

    public MutableLiveData<List<Workmate>> getWorkmateListLiveData() {
        return workmateListLiveData;
    }

    public LiveData<ErrorCode> getErrorCode() {
        return errorCode;
    }

    private String TAG = "WorkmateRepository";
    private ArrayList<Workmate> workmateList = new ArrayList<>();


    public void getWorkmateList() {
        executor.execute(() -> {
            Task<QuerySnapshot> task = workmateHelper.getAllWorkmates(firebaseFirestore);
            try {
                Tasks.await(task);
                QuerySnapshot queryDocumentSnapshots = task.getResult();

                List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                for (DocumentSnapshot snapshot : snapshotList) {
                    if ((snapshot.get("uid") != null)) {
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
                        }
                        workmateList.add(workmate);
                    }
                }

                if (!workmateList.isEmpty())
                    workmateListLiveData.postValue(workmateList);
            } catch (ExecutionException e) {
                errorCode.postValue(ErrorCode.EXECUTION_EXCEPTION);

            } catch (InterruptedException e) {
                errorCode.postValue(ErrorCode.INTERRUPTED_EXCEPTION);
            }
        });

    }

    public void getWorkmatesForRestaurant(MutableLiveData<Restaurant> restaurantMutableLiveData) {
        //FILTER VIA FIREBASE
        Restaurant restaurant = restaurantMutableLiveData.getValue();


        if (restaurant != null && !restaurant.getHasWorkmates()) {
            executor.execute(() -> {
                List<Workmate> workmatesFiltered = new ArrayList<>();

                Task<QuerySnapshot> task = workmateHelper.getWorkmatesForRestaurant(firebaseFirestore, restaurant.getId());

                try {
                    Tasks.await(task);
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot : snapshotList) {
                        Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                        workmatesFiltered.add(workmateToAdd);
                    }
                    //UPDATE RESTAURANTLIVEDATA

                    restaurant.setAttendees(workmatesFiltered);
                    restaurant.setHasWorkmates(true);
                    restaurantMutableLiveData.postValue(restaurant);


                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            });
        }

    }


    public void getWorkmatesForRestaurantList(MutableLiveData<List<Restaurant>> restaurantListMutableLiveData) {
        //FILTER VIA FIREBASE


        List<Restaurant> restaurantList = restaurantListMutableLiveData.getValue();
        executor.execute(() -> {

            if (restaurantList != null) {
                for (Restaurant restaurant : restaurantList) {
                    if (!restaurant.getHasWorkmates()) {
                        List<Workmate> workmatesFiltered = new ArrayList<>();
                        workmateHelper.getWorkmatesForRestaurant(firebaseFirestore, restaurant.getId()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                        Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                                        workmatesFiltered.add(workmateToAdd);
                                    }
                                    //UPDATE RESTAURANTLIVEDATA

                                    restaurant.setAttendees(workmatesFiltered);
                                    restaurant.setHasWorkmates(true);
                                    restaurantListMutableLiveData.postValue(restaurantList);

                                }
                            }

                        });
                    }
                }
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
