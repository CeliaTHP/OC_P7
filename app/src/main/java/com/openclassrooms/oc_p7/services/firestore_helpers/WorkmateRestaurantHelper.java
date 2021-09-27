package com.openclassrooms.oc_p7.services.firestore_helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.models.Restaurant;

public class WorkmateRestaurantHelper {

    private static final String TAG = "WorkmateRestaurantHelper";
    private static final String COLLECTION_NAME = "workmate_restaurants";


    // COLLECTION REFERENCE
    public static CollectionReference getWorkmateRestaurantsCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE
    public static Task<Void> addWorkmateRestaurant(Restaurant restaurant) {
        return getWorkmateRestaurantsCollection().document(restaurant.getId()).set(restaurant);
    }


    public static Task<DocumentSnapshot> getWorkmateRestaurant(Restaurant restaurant) {
        return getWorkmateRestaurantsCollection().document(restaurant.getId()).get();
    }

    // DELETE
    public static Task<Void> deleteRestaurant(Restaurant restaurant) {
        return getWorkmateRestaurantsCollection().document(restaurant.getId()).delete();
    }


}
