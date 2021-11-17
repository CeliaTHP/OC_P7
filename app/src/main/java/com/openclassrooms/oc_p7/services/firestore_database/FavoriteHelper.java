package com.openclassrooms.oc_p7.services.firestore_database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.models.Restaurant;

public class FavoriteHelper {

    private static final String TAG = "FavoriteHelper";
    private static final String COLLECTION_NAME = "favorites";


    // COLLECTION REFERENCE
    public static CollectionReference getFavoriteCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE
    public static Task<Void> addFavorite(Restaurant restaurant) {
        return getFavoriteCollection().document(restaurant.getId()).set(restaurant);
    }

    // GET
    public static Task<DocumentSnapshot> getFavorite(Restaurant restaurant) {
        return getFavoriteCollection().document(restaurant.getId()).get();
    }

    // DELETE
    public static Task<Void> deleteFavorite(Restaurant restaurant) {
        return getFavoriteCollection().document(restaurant.getId()).delete();
    }


}
