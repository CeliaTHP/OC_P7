package com.openclassrooms.oc_p7.view_models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WorkmateListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "WorkmateListViewModel";

    private ArrayList<Workmate> workmateList = new ArrayList<>();
    public MutableLiveData<List<Workmate>> workmateListLiveData = new MutableLiveData<>();


    public WorkmateListViewModel() {
    }

    public void initWorkmateList() {
        WorkmateHelper.getAllWorkmatesTask()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess");
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList) {
                            Workmate workmate = new Workmate(
                                    snapshot.get("uid").toString(),
                                    snapshot.get("name").toString(),
                                    snapshot.get("email").toString(),
                                    snapshot.get("picUrl").toString());
                            workmateList.add(workmate);
                        }
                        Log.d(TAG, workmateList + "");
                        workmateListLiveData.postValue(workmateList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Log.d(TAG, "onFailure");


                    }
                });
    }


}