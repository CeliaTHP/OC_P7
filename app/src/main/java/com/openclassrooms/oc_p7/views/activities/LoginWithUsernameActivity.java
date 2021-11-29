package com.openclassrooms.oc_p7.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.oc_p7.R;
import com.openclassrooms.oc_p7.databinding.ActivityLoginWithUsernameBinding;
import com.openclassrooms.oc_p7.services.firestore_database.UserDatabase;
import com.openclassrooms.oc_p7.view_models.LoginViewModel;

import org.jetbrains.annotations.NotNull;

public class LoginWithUsernameActivity extends AppCompatActivity {


    private ActivityLoginWithUsernameBinding activityLoginWithUsernameBinding;
    private LoginViewModel loginViewModel;

    private boolean isCreatingAccount = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginWithUsernameBinding = ActivityLoginWithUsernameBinding.inflate(LayoutInflater.from(this));

        setContentView(activityLoginWithUsernameBinding.getRoot());

        initViewModels();
        initListeners();
        initObservers();
        initUI();

    }


    private void initViewModels() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
    }

    private void initUI() {
        activityLoginWithUsernameBinding.loginTextSwitcher.setCurrentText(getString(R.string.login_title));
    }

    private void initListeners() {
        activityLoginWithUsernameBinding.loginLogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkNotEmpty()) {
                    if (isCreatingAccount) {
                        loginViewModel.createFirebaseAccountWithEmailAndPassword(activityLoginWithUsernameBinding.loginEmailField.getText().toString(),
                                activityLoginWithUsernameBinding.loginPasswordField.getText().toString(), new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast toast = Toast.makeText(LoginWithUsernameActivity.this, R.string.login_fail_creation, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM, 0, 20);
                                        toast.show();
                                    }
                                });
                    } else {
                        loginViewModel.signInWithEmailAndPassword(activityLoginWithUsernameBinding.loginEmailField.getText().toString(),
                                activityLoginWithUsernameBinding.loginPasswordField.getText().toString(), new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) {
                                        Toast toast = Toast.makeText(LoginWithUsernameActivity.this, R.string.login_fail, Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.BOTTOM, 0, 20);
                                        toast.show();
                                    }
                                });

                    }
                } else {
                    Toast toast = Toast.makeText(LoginWithUsernameActivity.this, R.string.login_missing_field, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 20);
                    toast.show();
                }
            }
        });

        activityLoginWithUsernameBinding.loginCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCreatingAccount) {
                    isCreatingAccount = false;
                    activityLoginWithUsernameBinding.loginTextSwitcher.setText(getString(R.string.login_title));
                    activityLoginWithUsernameBinding.loginCreateButton.setText(R.string.login_title_italic);
                    activityLoginWithUsernameBinding.loginLogButton.setText(R.string.login_title);

                } else {
                    isCreatingAccount = true;
                    activityLoginWithUsernameBinding.loginTextSwitcher.setText(getString(R.string.signup_title));
                    activityLoginWithUsernameBinding.loginCreateButton.setText(R.string.signup_title_italic);
                    activityLoginWithUsernameBinding.loginLogButton.setText(R.string.signup_title);


                }

            }
        });
    }

    private void initObservers() {
        loginViewModel.authenticatedUserLiveData.observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                UserDatabase.createUser(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(), "" + firebaseUser.getPhotoUrl());
                loginViewModel.initWorkmates();
                goToDashboard();

            }
        });
    }


    private Boolean checkNotEmpty() {
        return !TextUtils.isEmpty(activityLoginWithUsernameBinding.loginEmailField.getText()) && !TextUtils.isEmpty(activityLoginWithUsernameBinding.loginPasswordField.getText())
                && Patterns.EMAIL_ADDRESS.matcher(activityLoginWithUsernameBinding.loginEmailField.getText()).matches();
    }

    private void goToDashboard() {
        Intent intent = new Intent(LoginWithUsernameActivity.this, DashboardActivity.class);
        startActivity(intent);
    }
}