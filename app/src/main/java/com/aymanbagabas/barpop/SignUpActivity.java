package com.aymanbagabas.barpop;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;

public class SignUpActivity extends AppCompatActivity {

    private EditText first;
    private EditText last;
    private EditText email;
    private EditText password;
    private EditText date;
    private Button signupbutton;
    private RadioGroup gender;
    private AuthHelper authHelper;
    private CognitoUserPool userPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        first = (EditText) findViewById(R.id.first);
        last = (EditText) findViewById(R.id.last);
        email = (EditText) findViewById(R.id.email_signup);
        password = (EditText) findViewById(R.id.password_signup);
        date = (EditText) findViewById(R.id.birthdate);
        signupbutton = (Button) findViewById(R.id.sign_up_button);
        gender = (RadioGroup) findViewById(R.id.gender);

        signupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });

        authHelper = new AuthHelper(getBaseContext(), "us-east-2:3ff98a6f-db23-47a2-905b-16ac397378d3", "2vh33a0af73l6k6p85g30era40", "s11pi6l420b0npg6vp61c7dqkcg2f1sdnkas7q4dfgdjsmgrac9");

        userPool = authHelper.getUserPool();
    }

    private void attemptSignUp() {
        String name = String.format("%s %s", first.getText().toString(), last.getText().toString());
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();
        String birthdate = this.date.getText().toString();
        

        CognitoUserAttributes cognitoUserAttributes = authHelper.getUserAttributes();
        cognitoUserAttributes.addAttribute("name", name);
        cognitoUserAttributes.addAttribute("email", email);
        cognitoUserAttributes.addAttribute("gender", email);
        cognitoUserAttributes.addAttribute("birthdate", birthdate);

        userPool.signUpInBackground(email, password, cognitoUserAttributes, null, authHelper.getSignupCallback());

    }
}
