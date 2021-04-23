package com.openclassrooms.oc_p7.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.auth.AuthCredential;
import com.openclassrooms.oc_p7.model.User;

public class LoginViewModel extends AndroidViewModel {

    LiveData<User> authenticatedUserLiveData;


    private static String TAG = "LoginViewModel";

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        //Firebase Sign With Google
    }


}

