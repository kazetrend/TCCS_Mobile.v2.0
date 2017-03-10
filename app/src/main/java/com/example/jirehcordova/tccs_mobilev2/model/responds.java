package com.example.jirehcordova.tccs_mobilev2.model;

import java.io.Serializable;

/**
 * Created by Jireh Cordova on 06/03/2017.
 */
//this model handles the basic responses from JSON OBJECTS

public class responds implements Serializable {

    private String error;

    public responds(String error){
        this.error = error;
    }
    public responds(){}

    public String getError(){
        return error;
    }

    public void setError(String error){
        this.error = error;
    }
}
