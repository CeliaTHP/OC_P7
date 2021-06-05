package com.openclassrooms.oc_p7.service;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.model.User;

public class UserService {

    private static String TAG = "UserService";
    private static User user;

    public static void synchUser() {
        if (user != null) {
            Log.d(TAG, "synchUser");
            user.setName(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            user.setPicUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() + "");
            user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        } else createUser();
    }

    public static void createUser() {
        Log.d(TAG, "createUser");
        user = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), null, FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() + "", DummyWorkmateGenerator.generateWorkmates());
    }

    public static User getUser() {
        return user;
    }


}
