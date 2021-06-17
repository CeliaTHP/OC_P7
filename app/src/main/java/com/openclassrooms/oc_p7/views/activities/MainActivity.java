package com.openclassrooms.oc_p7.views.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.oc_p7.R;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    public static FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onResume() {
        super.onResume();
        verifyAuth();
    }

    private void verifyAuth() {
        //if not authenticated, go to LogInActivity
        //else go to MainActivity
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d("USER_SERVICE", "synchUser : " + user.getDisplayName());
            goToActivity(new DashboardActivity());
            Log.d(TAG, "User Logged : " + user.getDisplayName());
        } else {
            goToActivity(new LoginActivity());
            Log.d(TAG, "User Not Found");

        }
    }

    private void goToActivity(Activity activity) {
        Intent intent = new Intent(this, activity.getClass());
        startActivity(intent);
    }

}