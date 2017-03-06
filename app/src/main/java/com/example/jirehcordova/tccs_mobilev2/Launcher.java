package com.example.jirehcordova.tccs_mobilev2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.manusunny.pinlock.PinListener;

/**
 * Created by Jireh Cordova on 05/03/2017.
 */

public class Launcher extends Activity {

    public static final int REQUEST_CODE_CONFIRM_PIN = 2;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);

        if(firstRun){
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
            Intent i = new Intent(Launcher.this, LoginActivity.class);
            startActivity(i);
        }else{
            Intent intent = new Intent(Launcher.this, ConfirmPin.class);
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_PIN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CONFIRM_PIN : {
                if(resultCode == PinListener.SUCCESS){
                    Toast.makeText(this, "Pin is correct :)", Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(Launcher.this, MainActivity.class);
                    startActivity(k);
                } else if(resultCode == PinListener.CANCELLED) {
                    Toast.makeText(this, "Pin confirm cancelled :|", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }
}
