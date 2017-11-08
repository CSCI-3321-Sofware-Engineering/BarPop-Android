package com.aymanbagabas.barpop;

import android.content.Context;
import android.net.NetworkRequest;
import android.os.Build;
import android.support.v4.*;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;

/**
 * Created by matt on 11/8/2017.
 */

 public class AmazonServiceProvider extends ServiceProvider {
    String pool_id;
    String client_id;
    String client_secret;
    private CognitoUserPool userPool;
    private Context  context;
    AmazonServiceProvider(String poolId, String client_id, String client_secret){
         setPoolId(poolId);
         setClientId(client_id);
         setClientSecret(client_secret);
    }
    @Override
    public void login() {

    }

    public void setContext(Context context) {
       this.context = context;
    }
    public void setPoolId(String poolId) {
        this.pool_id = poolId;
    }

    public void setClientId(String clientId) {
        this.client_id = clientId;
    }
    public  void setClientSecret(String clientSecret) {
        this.client_secret = clientSecret;
    }

    @Override
    public void signUp() {
       AmazonCognitoIdentityProviderClient identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(), new ClientConfiguration());
       identityProviderClient.setRegion(Region.getRegion(Regions.US_EAST_2));

       userPool = new CognitoUserPool(context, pool_id, client_id, client_secret, identityProviderClient);
    }

}

