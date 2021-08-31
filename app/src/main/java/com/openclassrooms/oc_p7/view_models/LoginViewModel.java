package com.openclassrooms.oc_p7.view_models;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;
import com.openclassrooms.oc_p7.models.Workmate;
import com.openclassrooms.oc_p7.services.dummies.DummyWorkmateGenerator;
import com.openclassrooms.oc_p7.services.firestore_helpers.WorkmateHelper;
import com.openclassrooms.oc_p7.views.activities.LoginActivity;

import org.jetbrains.annotations.NotNull;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<FirebaseUser> authenticatedUserLiveData = new MutableLiveData<>();
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    private OAuthProvider.Builder provider;

    private static String TAG = "LoginViewModel";

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }


    public void createFirebaseAccountWithEmailAndPassword(String email, String password, OnFailureListener onFailureListener) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authenticatedUserLiveData.postValue(task.getResult().getUser());
                    Log.d(TAG, "firebaseAuth Success");
                } else {
                    onFailureListener.onFailure(task.getException());
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createAccountWithCredential:failure", task.getException());
                }
            }
        });

    }

    public void signInWithEmailAndPassword(String email, String password, OnFailureListener onFailureListener) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    authenticatedUserLiveData.postValue(task.getResult().getUser());
                    Log.d(TAG, "firebaseAuth Success");
                } else {
                    onFailureListener.onFailure(task.getException());
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                }
            }
        });

    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            authenticatedUserLiveData.postValue(task.getResult().getUser());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
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

    public void firebaseAuthWithTwitter(LoginActivity activity, OAuthProvider.Builder provider) {
        auth
                .startActivityForSignInWithProvider(activity, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.d(TAG, "Twitter Sign in success " + authResult.getAdditionalUserInfo().getUsername());
                                authenticatedUserLiveData.postValue(authResult.getUser());
                                // User is signed in.
                                // IdP data available in
                                // authResult.getAdditionalUserInfo().getProfile().
                                // The OAuth access token can also be retrieved:
                                // authResult.getCredential().getAccessToken().
                                // The OAuth secret can be retrieved by calling:
                                // authResult.getCredential().getSecret().
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle failure.
                                //TODO : Error dialog if no internet
                                Log.d(TAG, "Twitter Sign in failure " + e.getMessage());
                            }
                        });
    }

    public void getFacebookAccount(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "SUCCESS : " + loginResult.getAccessToken().getUserId());
                        firebaseAuthWithFacebook(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "CANCEL ");

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "ERROR : " + error.getMessage());

                    }
                });
    }

    public void getUsernameAccount() {
        //auth.signInWithEmailAndPassword();

    }

    public void getTwitterAccount(LoginActivity loginActivity) {
        provider = OAuthProvider.newBuilder("twitter.com");
        Task<AuthResult> pendingResultTask = auth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Log.d(TAG, "Twitter auth success");
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth secret can be retrieved by calling:
                                    // authResult.getCredential().getSecret().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                    Log.d(TAG, "Twitter auth failure");
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.
            firebaseAuthWithTwitter(loginActivity, provider);
        }
    }

    public void verifyAuth() {
        //if not authenticated, go to LogInActivity
        //else go to MainActivity
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            authenticatedUserLiveData.postValue(auth.getCurrentUser());
            Log.d(TAG, "User Logged : " + user.getDisplayName());
        }
    }

    public void initWorkmates() {
        for (Workmate workmate : DummyWorkmateGenerator.generateWorkmates()) {
            WorkmateHelper.createWorkmate(workmate.getUid(), workmate.getName(), workmate.getEmail(), workmate.getPicUrl());
            if (workmate.getRestaurantId() != null)
                WorkmateHelper.updateWorkmateRestaurantId(workmate.getRestaurantId(), workmate.getUid());
            if (workmate.getRestaurantName() != null)
                WorkmateHelper.updateWorkmateRestaurantName(workmate.getRestaurantName(), workmate.getUid());
        }
    }

    public void logOut() {
        FirebaseAuth.getInstance().signOut();

    }


}

