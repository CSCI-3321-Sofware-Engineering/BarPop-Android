package com.aymanbagabas.barpop;

import android.content.Context;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;

/**
 * Created by ayman on 10/22/17.
 */

public class AuthHelper {

    private ClientConfiguration clientConfiguration;
    private CognitoUserPool userPool;
    private CognitoUserAttributes userAttributes;

    private SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed, CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Sign-up was successful

            // Check if this user (cognitoUser) needs to be confirmed
            if(!userConfirmed) {
                Log.d("SignUp: ", "Success - need verification");
            }
            else {
                // The user has already been confirmed
                Log.d("SignUp: ", "Success");
            }
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-up failed, check exception for the cause

            Log.d("SignUp: ", "Failed");
        }
    };

    AuthHelper(Context context, String poolId, String clientId, String clientSecret) {
        clientConfiguration = new ClientConfiguration();
        // Create a CognitoUserPool object to refer to your user pool
        userPool = new CognitoUserPool(context, poolId, clientId, clientSecret);
        userAttributes = new CognitoUserAttributes();
    }

    public CognitoUserPool getUserPool() {
        return userPool;
    }

    public CognitoUserAttributes getUserAttributes() {
        return userAttributes;
    }

    public SignUpHandler getSignupCallback() {
        return signupCallback;
    }
}
