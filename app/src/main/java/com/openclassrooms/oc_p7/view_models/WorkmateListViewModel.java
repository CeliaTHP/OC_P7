package com.openclassrooms.oc_p7.view_models;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class WorkmateListViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private static String TAG = "WorkmateListViewModel";

    public MutableLiveData<Location> workmatesLiveData;


    public WorkmateListViewModel() {
    }

    public void initWorkmateList() {
        WorkmateHelper.getAllWorkmatesTask()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot snapshot : snapshotList) {
                            Log.d(TAG, snapshot.toString());
                        }

                        Log.d(TAG, "onSuccess");

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