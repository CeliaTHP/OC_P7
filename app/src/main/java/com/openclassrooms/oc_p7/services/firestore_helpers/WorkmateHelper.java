package com.openclassrooms.oc_p7.services.firestore_helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.openclassrooms.oc_p7.models.Workmate;


public class WorkmateHelper {

    private static final String TAG = "WorkmateHelper";
    private static final String COLLECTION_NAME = "workmates";

    // COLLECTION REFERENCE
    public static CollectionReference getWorkmatesCollection(FirebaseFirestore firebaseFirestore) {
        return firebaseFirestore.collection(COLLECTION_NAME);
    }

    public static Task<QuerySnapshot> getAllWorkmates(FirebaseFirestore firebaseFirestore) {
        return firebaseFirestore.collection(COLLECTION_NAME).get();
    }

    // CREATE
    public static Task<Void> createWorkmate(FirebaseFirestore firebaseFirestore, String uid, String name, String email, String picUrl) {
        Workmate workmateToCreate = new Workmate(uid, name, email, picUrl);
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).set(workmateToCreate);
    }

    // GET
    public static Task<DocumentSnapshot> getWorkmate(FirebaseFirestore firebaseFirestore, String uid) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).get();
    }

    public static Task<QuerySnapshot> getWorkmatesForRestaurant(FirebaseFirestore firebaseFirestore, String restaurantId) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).whereEqualTo("restaurantId", restaurantId).get();
    }

    // UPDATE
    public static Task<Void> updateWorkmateName(FirebaseFirestore firebaseFirestore, String name, String uid) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).update("name", name);
    }

    public static Task<Void> updateWorkmateRestaurantId(FirebaseFirestore firebaseFirestore, String restaurantId, String uid) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).update("restaurantId", restaurantId);
    }

    public static Task<Void> updateWorkmateRestaurantName(FirebaseFirestore firebaseFirestore, String restaurantName, String uid) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).update("restaurantName", restaurantName);
    }

    // UPDATE
    public static Task<Void> updateWorkmateRestaurantType(FirebaseFirestore firebaseFirestore, String restaurantType, String uid) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).update("restaurantType", restaurantType);
    }

    // DELETE WORKMATE
    public static Task<Void> deleteWorkmate(FirebaseFirestore firebaseFirestore, String uid) {
        return WorkmateHelper.getWorkmatesCollection(firebaseFirestore).document(uid).delete();
    }

}
