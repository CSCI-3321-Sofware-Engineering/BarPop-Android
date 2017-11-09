package com.aymanbagabas.barpop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidentityprovider.AmazonCognitoIdentityProviderClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {

    private UserSignUpTask signUpTask = null;

    private View signUpProgress;
    private View signUpForm;
    private EditText firstView;
    private EditText lastView;
    private EditText emailView;
    private EditText passwordView;
    private EditText dateView;
    private Button signUpButton;
    private RadioGroup genderViewGroup;
    private RadioButton male;
    private RadioButton female;
    private AuthHelper authHelper;
    private CognitoUserPool userPool;

    private ServiceProvider serviceProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpProgress = findViewById(R.id.signup_progress);
        signUpForm = findViewById(R.id.sign_up_form);

        firstView = (EditText) findViewById(R.id.first);
        lastView = (EditText) findViewById(R.id.last);
        emailView = (EditText) findViewById(R.id.email_signup);
        passwordView = (EditText) findViewById(R.id.password_signup);
        dateView = (EditText) findViewById(R.id.birthdate);

        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(calendar);
            }
        };

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(v.getContext(), date,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        signUpButton = (Button) findViewById(R.id.sign_up_button);
        genderViewGroup = (RadioGroup) findViewById(R.id.gender);
        male = (RadioButton) findViewById(R.id.male);
        female = (RadioButton) findViewById(R.id.female);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });



        /*AmazonCognitoIdentityProviderClient identityProviderClient = new AmazonCognitoIdentityProviderClient(new AnonymousAWSCredentials(), new ClientConfiguration());
        identityProviderClient.setRegion(Region.getRegion(Regions.US_EAST_2));

        userPool = new CognitoUserPool(getBaseContext(), POOL_ID, CLIENT_ID, CLIENT_SECRET, identityProviderClient);

        AmazonServiceProvider amazonServiceProvider = new AmazonServiceProvider();
        */
        initServiceProvider();

    }

    void initServiceProvider() {
        final String poolId = BuildConfig.amazon_pool_id;
        final String clientId = BuildConfig.amazon_client_id;
        final String clientSecret = BuildConfig.amazon_client_secret;
        serviceProvider = new AmazonServiceProvider(getApplicationContext(), poolId, clientId, clientSecret);
    }


    private void attemptSignUp() {
        String firstName = firstView.getText().toString();
        String lastName = lastView.getText().toString();
        String fullName = String.format("%s %s", firstName, lastName);
        String gender = "";
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String birthdate = dateView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(firstName)) {
            firstView.setError(getString(R.string.error_field_required));
            focusView = firstView;
            cancel = true;
        }

        if (TextUtils.isEmpty(lastName)) {
            lastView.setError(getString(R.string.error_field_required));
            focusView = lastView;
            cancel = true;
        }

        switch (genderViewGroup.getCheckedRadioButtonId()) {
            case R.id.male:
                gender = "male";
                break;
            case R.id.female:
                gender = "female";
                break;
            default:
                focusView = genderViewGroup;
                cancel = true;
        }

        if (TextUtils.isEmpty(gender) || gender.equals("")) {
            Toast.makeText(getBaseContext(), "Please select a gender", Toast.LENGTH_SHORT);
            focusView = genderViewGroup;
            cancel = true;
        }

        if (TextUtils.isEmpty(birthdate)) {
            dateView.setError(getString(R.string.error_field_required));
            focusView = dateView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            passwordView.setError(getString(R.string.error_invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            signUpTask = new SignUpActivity.UserSignUpTask(new Account(email, password, fullName, gender, birthdate));
            signUpTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        boolean foundUpper = false; boolean foundLower = false; boolean foundDigit = false;
        for(int i = 0; i < password.length(); i++) {
            char ch = password.charAt(i);
            if (Character.isUpperCase(ch)) foundUpper = true;
            if (Character.isLowerCase(ch)) foundLower = true;
            if (Character.isDigit(ch)) foundDigit = true;
        }

        return password.length() > 7 && foundDigit && foundLower && foundUpper;
    }

    private void updateLabel(Calendar calendar) {
        String dateFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.US);

        dateView.setText(sdf.format(calendar.getTime()));
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signUpForm.setVisibility(show ? View.GONE : View.VISIBLE);
            signUpForm.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signUpForm.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            signUpProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            signUpProgress.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signUpProgress.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            signUpProgress.setVisibility(show ? View.VISIBLE : View.GONE);
            signUpForm.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {

        private final Account account;

        UserSignUpTask(Account account) {
            this.account = account;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            boolean auth = false;

            ((AmazonServiceProvider)serviceProvider).signUp(account);

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            signUpTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                serviceProvider.onSignUpSuccess();
            }
        }

        @Override
        protected void onCancelled() {
            signUpTask = null;
            showProgress(false);
        }
    }
}
