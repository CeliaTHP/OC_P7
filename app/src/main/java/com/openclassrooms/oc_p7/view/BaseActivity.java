package com.openclassrooms.oc_p7.view;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;

import org.jetbrains.annotations.NotNull;

public abstract class BaseActivity extends AppCompatActivity {


    //ERROR HANDLER

    protected OnFailureListener onFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }


}
