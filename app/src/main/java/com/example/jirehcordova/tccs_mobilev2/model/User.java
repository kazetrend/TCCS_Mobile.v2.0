package com.example.jirehcordova.tccs_mobilev2.model;

import java.io.Serializable;

/**
 * Created by Jireh Cordova on 26/02/2017.
 */

public class User implements Serializable {
    private int id;
    private String firstName;
    private String lastname;
    private String email;
    private boolean isFirstLogin;
    public User(int id, String firstName, String lastname, boolean isFirstLogin, String hashedPin, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastname = lastname;
        this.isFirstLogin = isFirstLogin;
        this.hashedPin = hashedPin;
        this.email = email;
    }

    private String hashedPin;

    public User() {
    }

    public String getemail() {
        return email;
    }

    public void setemail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstName;
    }

    public void setFirstname(String firstname) {
        this.firstName= firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public boolean getisFirstlogin() {
        return isFirstLogin;
    }

    public void setisFirstlogin(boolean isFirstLogin) {
        this.isFirstLogin= isFirstLogin;
    }

    public String getHashedPin() {
        return hashedPin;
    }

    public void setHashedPin(String hashedPin) {
        this.hashedPin = hashedPin;
    }
}
