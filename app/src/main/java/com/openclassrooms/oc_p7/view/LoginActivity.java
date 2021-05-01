package com.openclassrooms.oc_p7.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
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
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.*;
import com.openclassrooms.oc_p7.view_model.LoginViewModel;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_GOOGLE_SIGN_IN = 1;


    private ActivityLoginBinding activityLoginBinding;
    private LoginViewModel loginViewModel;

    private static FirebaseAuth auth;
    private OAuthProvider.Builder provider;

    private GoogleSignInClient googleSignInClient;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(LayoutInflater.from(this), null, false);
        setContentView(activityLoginBinding.getRoot());

        initLoginViewModel();
        loginViewModel.verifyAuth();
        initLoginButton();
        initGoogleClient();
        initObservers();

    }


    private void initLoginButton() {
        activityLoginBinding.loginGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });
        activityLoginBinding.loginFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithFacebook();
            }
        });
        activityLoginBinding.loginTwitterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithTwitter();
            }
        });
    }

    private void initLoginViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void signInWithGoogle() {
        Intent getGoogleAccount = googleSignInClient.getSignInIntent();
        startActivityForResult(getGoogleAccount, RC_GOOGLE_SIGN_IN);
    }

    private void signInWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));
        getFacebookAccount();
    }

    private void signInWithTwitter() {
        provider = OAuthProvider.newBuilder("twitter.com");
        getTwitterAccount();
    }

    private void initGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getApplication().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            Exception exception = task.getException();
            if (task.isSuccessful()) {
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                    loginViewModel.firebaseAuthWithGoogle(account.getIdToken());
                    //firebaseAuthWithGoogle(account.getIdToken());
                } catch (ApiException e) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e);
                }

            } else if (exception != null) {
                Log.d(TAG, exception.toString());
            }

        } else {
            if (callbackManager != null)
                callbackManager.onActivityResult(requestCode, resultCode, data);

        }
    }


    private void getFacebookAccount() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d(TAG, "SUCCESS : " + loginResult.getAccessToken().getUserId());
                        firebaseAuthWithFacebook(loginResult.getAccessToken());
                        goToDashboard();
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



/*
    private void getFacebookAccount() {

        // Initialize Facebook Login button
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = activityLoginBinding.loginFacebookButton;
        loginButton.setPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
            }
        });
    }
 */


    private void getTwitterAccount() {
        Task<AuthResult> pendingResultTask = loginViewModel.auth.getPendingAuthResult();
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
            firebaseAuthWithTwitter();
        }
    }

    private void firebaseAuthWithTwitter() {
        loginViewModel.auth
                .startActivityForSignInWithProvider(this, provider.build())
                .addOnSuccessListener(
                        new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Log.d(TAG, "Twitter Sign in success " + authResult.getAdditionalUserInfo().getUsername());
                                loginViewModel.authenticatedUserLiveData.postValue(authResult.getUser());
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
                                Log.d(TAG, "Twitter Sign in failure " + e.getMessage());
                            }
                        });
    }

    private void firebaseAuthWithFacebook(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            goToDashboard();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }


    private void initObservers() {
        loginViewModel.authenticatedUserLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null) goToDashboard();
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
        startActivity(intent);
    }


}