package com.openclassrooms.oc_p7.services.firestore_database;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.openclassrooms.oc_p7.models.User;


public class UserDao {

    private static final String COLLECTION_NAME = "users";

    // COLLECTION REFERENCE
    public static CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // CREATE
    public static Task<Void> createUser(String uid, String name, String email, String picUrl) {
        User userToCreate = new User(uid, name, email, picUrl);
        return UserDao.getUsersCollection().document(uid).set(userToCreate);
    }

    // GET
    public static Task<DocumentSnapshot> getUser(String uid) {
        return UserDao.getUsersCollection().document(uid).get();
    }

    // UPDATE
    public static Task<Void> updateUserName(String name, String uid) {
        return UserDao.getUsersCollection().document(uid).update("name", name);
    }

    public static Task<Void> updateUserRestaurantId(String restaurantId, String uid) {
        return UserDao.getUsersCollection().document(uid).update("restaurantId", restaurantId);
    }

    public static Task<Void> updateUserRestaurantName(String restaurantName, String uid) {
        return UserDao.getUsersCollection().document(uid).update("restaurantName", restaurantName);
    }

    public static Task<Void> updateUserRestaurantAddress(String restaurantAddress, String uid) {
        return UserDao.getUsersCollection().document(uid).update("restaurantAddress", restaurantAddress);
    }

    // DELETE
    public static Task<Void> deleteUser(String uid) {
        return UserDao.getUsersCollection().document(uid).delete();
    }


}
