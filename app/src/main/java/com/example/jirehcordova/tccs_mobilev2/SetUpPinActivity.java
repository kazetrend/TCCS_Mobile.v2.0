package com.example.jirehcordova.tccs_mobilev2;

import android.content.SharedPreferences;
import android.util.Log;

import com.manusunny.pinlock.SetPinActivity;

/**
 * Created by Jireh Cordova on 03/03/2017.
 */

public class SetUpPinActivity extends SetPinActivity {

    @Override
    public void onPinSet(String pin) {
        String showPin;
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("AppPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("pinCode", pin);
        editor.commit();
        Log.d("showthepin", showPin = preferences.getString("pinCode", ""));
        setResult(SUCCESS);
        finish();
    }
}
