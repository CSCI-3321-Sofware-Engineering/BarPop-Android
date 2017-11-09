package com.aymanbagabas.barpop;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;

/**
 * Created by matt on 11/8/2017.
 */

public class Account  {
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

    String firstName = firstView.getText().toString();
    String lastName = lastView.getText().toString();
    String fullName = String.format("%s %s", firstName, lastName);
    String gender = "";
    String email = emailView.getText().toString();
    String password = passwordView.getText().toString();
    String birthdate = dateView.getText().toString();


    //getters and setters
    public View getSignUpProgress() {
        return signUpProgress;
    }

    public void setSignUpProgress(View signUpProgress) {
        this.signUpProgress = signUpProgress;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


}
