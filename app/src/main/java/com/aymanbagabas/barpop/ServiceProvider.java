package com.aymanbagabas.barpop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

abstract public class ServiceProvider {

    private Context context;
    private AlertDialog userDialog;

    ServiceProvider(Context context) {
        this.context = context;
    }

    abstract public void login(Account account);
    abstract public void signUp(Account account);

    protected void showDialogMessage(String title, String body) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(body).setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    userDialog.dismiss();

                } catch (Exception e) {
                    // Log failure
                    Log.e(TAG,"Dialog failure", e);
                }
            }
        });
        userDialog = builder.create();
        userDialog.show();
    }

    public void onLoginSuccess() {
        showDialogMessage("Login", "Success");
    }

    public void onLoginFailure() {
        showDialogMessage("Login", "Failed");
    }

    public void onSignUpSuccess() {
        showDialogMessage("SignUp", "Success");
    }

    public void onSignUpFalure() {
        showDialogMessage("SignUp", "Failed");
    }
}
