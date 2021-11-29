package com.openclassrooms.oc_p7.services.firestore_database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.Workmate;

import org.jetbrains.annotations.NotNull;


public class WorkmateDatabase {

    private static final String COLLECTION_NAME = "workmates";

    // COLLECTION REFERENCE
    public CollectionReference getWorkmatesCollection(FirebaseFirestore firebaseFirestore) {
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    public Task<QuerySnapshot> getAllWorkmates(FirebaseFirestore firebaseFirestore) {
        return firebaseFirestore.collection(COLLECTION_NAME).get();
    }

    // CREATE
    public Task<Void> createWorkmate(FirebaseFirestore firebaseFirestore, String uid, String name, String email, String picUrl) {
        Workmate workmateToCreate = new Workmate(uid, name, email, picUrl);
        return getWorkmatesCollection(firebaseFirestore).document(uid).set(workmateToCreate);
    }

    // GET
    @NotNull
    public Task<DocumentSnapshot> getWorkmate(FirebaseFirestore firebaseFirestore, String uid) {
        return getWorkmatesCollection(firebaseFirestore).document(uid).get();
    }

    public Task<QuerySnapshot> getWorkmatesForRestaurant(FirebaseFirestore firebaseFirestore, String restaurantId) {
        return getWorkmatesCollection(firebaseFirestore).whereEqualTo("restaurantId", restaurantId).get();
    }

    // UPDATE
    public Task<Void> updateWorkmateName(FirebaseFirestore firebaseFirestore, String name, String uid) {
        return getWorkmatesCollection(firebaseFirestore).document(uid).update("name", name);
    }

    public Task<Void> updateWorkmateRestaurantId(FirebaseFirestore firebaseFirestore, String restaurantId, String uid) {
        return getWorkmatesCollection(firebaseFirestore).document(uid).update("restaurantId", restaurantId);
    }

    public Task<Void> updateWorkmateRestaurantName(FirebaseFirestore firebaseFirestore, String restaurantName, String uid) {
        return getWorkmatesCollection(firebaseFirestore).document(uid).update("restaurantName", restaurantName);
    }

    // UPDATE
    public Task<Void> updateWorkmateRestaurantType(FirebaseFirestore firebaseFirestore, String restaurantType, String uid) {
        return getWorkmatesCollection(firebaseFirestore).document(uid).update("restaurantType", restaurantType);
    }

    // DELETE
    public Task<Void> deleteWorkmate(FirebaseFirestore firebaseFirestore, String uid) {
        return getWorkmatesCollection(firebaseFirestore).document(uid).delete();
    }

}
