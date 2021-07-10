package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.google.firebase.auth.FirebaseAuth;
import com.openclassrooms.oc_p7.databinding.ActivitySettingsBinding;
import com.openclassrooms.oc_p7.services.firestore_helpers.UserHelper;


public class SettingsActivity extends BaseActivity {

    ActivitySettingsBinding activitySettingsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySettingsBinding = ActivitySettingsBinding.inflate(LayoutInflater.from(this), null, false);

        initListeners();
        setContentView(activitySettingsBinding.getRoot());
    }

    private void initListeners() {
        activitySettingsBinding.settingsDeleteAccountButton.setOnClickListener(v -> {
            deleteUserFromFirebase();
            logOut();
            goToLogin();
        });
    }

    private void deleteUserFromFirebase() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            UserHelper.deleteUser(FirebaseAuth.getInstance().getUid()).addOnFailureListener(
                    this.onFailureListener());
        }
    }


    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}