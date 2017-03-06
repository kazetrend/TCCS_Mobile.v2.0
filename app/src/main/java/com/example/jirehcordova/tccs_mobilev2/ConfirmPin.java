package com.example.jirehcordova.tccs_mobilev2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.manusunny.pinlock.ConfirmPinActivity;

/**
 * Created by Jireh Cordova on 03/03/2017.
 */

public class ConfirmPin extends ConfirmPinActivity {
    private String currentPin;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        SharedPreferences conn = getSharedPreferences("AppPref", MODE_PRIVATE);
        currentPin = conn.getString("pinCode","");
    }

    @Override
    public boolean isPinCorrect(String pin) {
        /*SharedPreferences confirm = getSharedPreferences("AppPref", MODE_PRIVATE);
        String currentPin = confirm.getString("pinCode", "");*/
        return pin.equals(currentPin);
    }

    @Override
    public void onForgotPin() {
        Toast.makeText(this, "lol! What a loser!!", Toast.LENGTH_SHORT).show();
        /*new MaterialDialog.Builder(this)
                .content("Make a new Pin?")
                .positiveText("I'd like to make a new pin")
                .negativeText("No. I remember it now.")
                .show();*/
    }
}
