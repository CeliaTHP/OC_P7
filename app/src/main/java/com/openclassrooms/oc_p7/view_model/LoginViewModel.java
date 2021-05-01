package com.openclassrooms.oc_p7.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.openclassrooms.oc_p7.model.User;
import com.openclassrooms.oc_p7.view.LoginActivity;

import java.security.Provider;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<FirebaseUser> authenticatedUserLiveData = new MutableLiveData<>();
    public FirebaseAuth auth;
    OAuthProvider.Builder provider;


    private static String TAG = "LoginViewModel";

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public Boolean isUserConnected() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    public String getUserDisplayName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }



     public void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            authenticatedUserLiveData.postValue(task.getResult().getUser());
                            Log.d(TAG, "signInWithCredential:success");
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }



    public void verifyAuth() {
        //if not authenticated, go to LogInActivity
        //else go to MainActivity
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            authenticatedUserLiveData.postValue(auth.getCurrentUser());
            Log.d(TAG, "User Logged : " + user.getDisplayName());
        } else {
            Log.d(TAG, "User Not Found");

        }
    }






}

