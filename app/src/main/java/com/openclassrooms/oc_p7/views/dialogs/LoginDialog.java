package com.openclassrooms.oc_p7.views.dialogs;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.openclassrooms.oc_p7.databinding.DialogLoginUsernameBinding;

public class LoginDialog {


    public static AlertDialog showDialog(Context context, Boolean isCreating) {
        DialogLoginUsernameBinding dialogLoginUsernameBinding = DialogLoginUsernameBinding.inflate(LayoutInflater.from(context));
        AlertDialog dialog = new MaterialAlertDialogBuilder(context).create();

        dialog.setView(dialogLoginUsernameBinding.getRoot());


        return dialog;
    }
}
