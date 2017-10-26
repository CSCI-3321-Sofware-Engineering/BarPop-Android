package com.aymanbagabas.barpop;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;

/**
 * Created by ayman on 10/22/17.
 */

public class AuthHelper {

    private ClientConfiguration clientConfiguration;
    private CognitoUserPool userPool;

    AuthHelper(String poolId, String clientId, String clientSecret) {
        clientConfiguration = new ClientConfiguration();
        // Create a CognitoUserPool object to refer to your user pool
        //userPool = new CognitoUserPool(context, poolId, clientId, clientSecret, clientConfiguration);
        //userPool = new CognitoUserPool();
    }
}
