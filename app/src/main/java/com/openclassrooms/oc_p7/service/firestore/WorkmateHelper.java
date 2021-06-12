package com.openclassrooms.oc_p7.service.firestore;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.model.Workmate;


public class WorkmateHelper {

    private static final String COLLECTION_NAME = "workmates";

    // COLLECTION REFERENCE
    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE
    public static Task<Void> createWorkmate(String uid, String name, String email, String picUrl) {
        Workmate workmateToCreate = new Workmate(uid, name, email, picUrl);
        return WorkmateHelper.getWorkmatesCollection().document(uid).set(workmateToCreate);
    }

    // GET
    public static Task<DocumentSnapshot> getWorkmate(String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).get();
    }

    // UPDATE
    public static Task<Void> updateWorkmateName(String name, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update("name", name);
    }

    public static Task<Void> updateWorkmateRestaurantName(String restaurantName, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update("restaurantName", restaurantName);
    }

    // UPDATE
    public static Task<Void> updateWorkmateRestaurantType(String restaurantType, String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).update("restaurantType", restaurantType);
    }

    // DELETE
    public static Task<Void> deleteWorkmate(String uid) {
        return WorkmateHelper.getWorkmatesCollection().document(uid).delete();
    }

}
