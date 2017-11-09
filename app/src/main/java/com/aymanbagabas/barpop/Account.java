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

public class Account {

    private String name;
    private String gender;
    private String email;
    private String password;
    private String birthdate;

    public Account() {}

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Account(String email, String password, String name, String gender, String dateOfBirth) {
        this(email, password);
        setName(name); setGender(gender); setBirthdate(dateOfBirth);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}
