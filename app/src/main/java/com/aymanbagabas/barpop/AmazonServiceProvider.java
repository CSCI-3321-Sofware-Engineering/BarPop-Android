package com.aymanbagabas.barpop;

import android.content.Context;
import android.util.Log;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
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
    private Context context;
    private AmazonCognitoIdentityProviderClient identityProviderClient;
    private AuthenticationHandler authHandler;

    private SignUpHandler signupCallback = new SignUpHandler() {

        @Override
        public void onSuccess(CognitoUser cognitoUser, boolean userConfirmed,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            // Sign-up was successful

            // Check if this user (cognitoUser) needs to be confirmed
            if(!userConfirmed) {
                showDialogMessage("Confirmation link sent!", "Do not forget to verify your account.");
            }
            else {
                // The user has already been confirmed
                Log.d("-- SignUp: ", "Success");
            }
        }

        @Override
        public void onFailure(Exception exception) {
            // Sign-up failed, check exception for the cause
            showDialogMessage("Error!", exception.getMessage());
        }
    };

    AmazonServiceProvider(Context context, String poolId, String client_id, String client_secret){
        super(context);
        setPoolId(poolId);
        setClientId(client_id);
        setClientSecret(client_secret);
        identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(), new ClientConfiguration());
        identityProviderClient.setRegion(Region.getRegion(Regions.US_EAST_2));
        userPool = new CognitoUserPool(context, pool_id, client_id, client_secret, identityProviderClient);
    }

    public void setAuthHandler(AuthenticationHandler authenticationHandler) {
        authHandler = authenticationHandler;
    }

    @Override
    public void login(Account account) {
        final String username = account.getEmail();
        final String password = account.getPassword();

        CognitoUser cognitoUser = userPool.getUser(username);

        final String clientPassword = password;
        // Callback handler for the sign-in process
        AuthenticationHandler authenticationHandler = authHandler;

        // Sign in the user
        cognitoUser.getSessionInBackground(authenticationHandler);

        // Implement callback handler for getting details
        GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
            @Override
            public void onSuccess(CognitoUserDetails cognitoUserDetails) {
                // The user detail are in cognitoUserDetails
                Log.d("-- Login", cognitoUserDetails.getAttributes().getAttributes().get("birthdate"));
            }

            @Override
            public void onFailure(Exception exception) {
                // Fetch user details failed, check exception for the cause
            }
        };

// Fetch the user details
        cognitoUser.getDetailsInBackground(getDetailsHandler);
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


    //get signup informaiton and call the sign up function
    @Override
    public void signUp(Account account) {
        String email = account.getEmail();
        String password = account.getPassword();
        String name = account.getName();
        String gender = account.getGender();
        String birthdate = account.getBirthdate();

        CognitoUserAttributes cognitoUserAttributes = new CognitoUserAttributes();
        cognitoUserAttributes.addAttribute("name", name);
        cognitoUserAttributes.addAttribute("email", email);
        cognitoUserAttributes.addAttribute("gender", gender);
        cognitoUserAttributes.addAttribute("birthdate", birthdate);

        userPool.signUpInBackground(email, password, cognitoUserAttributes, null, signupCallback);
    }
}


