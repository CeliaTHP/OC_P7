package com.openclassrooms.oc_p7.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.ErrorCode;
import com.openclassrooms.oc_p7.models.Restaurant;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.firestore_database.WorkmateDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class WorkmateRepository {

    private Executor executor;
    private FirebaseFirestore firebaseFirestore;
    private MutableLiveData<ErrorCode> errorCode;

    private MutableLiveData<List<Workmate>> workmateListLiveData;
    private WorkmateDatabase workmateDatabase;

    public WorkmateRepository(FirebaseFirestore firebaseFirestore, WorkmateDatabase workmateDatabase, Executor executor, MutableLiveData<List<Workmate>> workmateListLiveData, MutableLiveData<ErrorCode> errorCode) {
        this.firebaseFirestore = firebaseFirestore;
        this.workmateDatabase = workmateDatabase;
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

    private ArrayList<Workmate> workmateList = new ArrayList<>();


    //Get the user's workmates list from firestore
    public void getWorkmateList() {
        //Executor to execute the following code in the same thread (easier for tests)
        executor.execute(() -> {
            Task<QuerySnapshot> task = workmateDatabase.getAllWorkmates(firebaseFirestore);
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

    //Get the corresponding user's workmates list according to a specific restaurant
    public void getWorkmatesForRestaurant(MutableLiveData<Restaurant> restaurantMutableLiveData) {
        Restaurant restaurant = restaurantMutableLiveData.getValue();
        if (restaurant != null && !restaurant.getHasWorkmates()) {
            //Executor to execute the following code in the same thread (easier for tests)
            executor.execute(() -> {
                List<Workmate> workmatesFiltered = new ArrayList<>();

                Task<QuerySnapshot> task = workmateDatabase.getWorkmatesForRestaurant(firebaseFirestore, restaurant.getId());

                try {
                    Tasks.await(task);
                    QuerySnapshot queryDocumentSnapshots = task.getResult();
                    List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot : snapshotList) {
                        Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                        workmatesFiltered.add(workmateToAdd);
                    }
                    restaurant.setAttendees(workmatesFiltered);
                    restaurant.setHasWorkmates(true);
                    restaurantMutableLiveData.postValue(restaurant);

                } catch (ExecutionException e) {
                    errorCode.postValue(ErrorCode.EXECUTION_EXCEPTION);

                } catch (InterruptedException e) {
                    errorCode.postValue(ErrorCode.INTERRUPTED_EXCEPTION);
                }
            });
        }


    }

    //Get the corresponding user's workmates list according to a specific restaurant list
    public void getWorkmatesForRestaurantList(MutableLiveData<List<Restaurant>> restaurantListMutableLiveData) {
        List<Restaurant> restaurantList = restaurantListMutableLiveData.getValue();
        if (restaurantList != null) {
            //Executor to execute the following code in the same thread (easier for tests)
            executor.execute(() -> {
                for (Restaurant restaurant : restaurantList) {
                    if (!restaurant.getHasWorkmates()) {
                        List<Workmate> workmatesFiltered = new ArrayList<>();
                        Task<QuerySnapshot> task = workmateDatabase.getWorkmatesForRestaurant(firebaseFirestore, restaurant.getId());
                        try {
                            Tasks.await(task);
                            QuerySnapshot queryDocumentSnapshots = task.getResult();
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();

                            for (DocumentSnapshot documentSnapshot : snapshotList) {
                                Workmate workmateToAdd = documentSnapshot.toObject(Workmate.class);
                                workmatesFiltered.add(workmateToAdd);
                            }
                            restaurant.setAttendees(workmatesFiltered);
                            restaurant.setHasWorkmates(true);
                            restaurantListMutableLiveData.postValue(restaurantList);
                        } catch (ExecutionException e) {
                            errorCode.postValue(ErrorCode.EXECUTION_EXCEPTION);
                        } catch (InterruptedException e) {
                            errorCode.postValue(ErrorCode.INTERRUPTED_EXCEPTION);
                        }

                    }
                }
            });

        }

    }

}
