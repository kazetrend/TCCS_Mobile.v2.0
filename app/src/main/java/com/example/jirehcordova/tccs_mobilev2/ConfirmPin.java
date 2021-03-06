package com.example.jirehcordova.tccs_mobilev2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.manusunny.pinlock.ConfirmPinActivity;
import com.manusunny.pinlock.PinListener;
/**
 * Created by Jireh Cordova on 03/03/2017.
 */

public class ConfirmPin extends ConfirmPinActivity {
    private String currentPin;
    public static final int REQUEST_CODE_CONFIRM_PIN = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences conn = getSharedPreferences("AppPref", MODE_PRIVATE);
        currentPin = conn.getString("pinCode","");
    }

    @Override
    public boolean isPinCorrect(String pin) {
        //checks if pin matches the value from shared preferences
        Log.d("pol", currentPin);
        return pin.equals(currentPin);
    }

    @Override
    public void onForgotPin() {
//
    Toast.makeText(this, "Contact Admin", Toast.LENGTH_SHORT).show();
    }



}
