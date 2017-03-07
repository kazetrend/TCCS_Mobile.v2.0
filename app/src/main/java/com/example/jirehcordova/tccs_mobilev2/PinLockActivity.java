package com.example.jirehcordova.tccs_mobilev2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manusunny.pinlock.PinListener;

public class PinLockActivity extends Activity{

    public static final int REQUEST_CODE_SET_PIN = 0;
    public static final int REQUEST_CODE_CHANGE_PIN = 1;
    public static final int REQUEST_CODE_CONFIRM_PIN = 2;
    static SharedPreferences pinLockPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pinLockPrefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        init();
    }

    private void init() {
        TextView setPin = (TextView) findViewById(R.id.set_pin);
        TextView confirmPin = (TextView) findViewById(R.id.confirm_pin);

        String pin = pinLockPrefs.getString("pinCode", "");
        if (pin.equals("")) {
            confirmPin.setEnabled(false);
        } else {
            setPin.setText("Change PIN");

        }

        View.OnClickListener clickListener = getOnClickListener();
        setPin.setOnClickListener(clickListener);
        confirmPin.setOnClickListener(clickListener);
    }

    @NonNull
    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                String pin = pinLockPrefs.getString("pinCode", "");

                if (id == R.id.set_pin && pin.equals("")) {
                    Intent intent = new Intent(PinLockActivity.this, SetUpPinActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SET_PIN);
                } else if (id == R.id.set_pin) {
                    Intent intent = new Intent(PinLockActivity.this, ConfirmPin.class);
                    startActivityForResult(intent, REQUEST_CODE_CHANGE_PIN);
                } else if (id == R.id.confirm_pin) {
                    Intent intent = new Intent(PinLockActivity.this, ConfirmPin.class);
                    startActivityForResult(intent, REQUEST_CODE_CONFIRM_PIN);
                }
            }
        };
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SET_PIN : {
                if(resultCode == PinListener.SUCCESS){
                    Toast.makeText(this, "Pin Created Successfully ", Toast.LENGTH_SHORT).show();
                } else if(resultCode == PinListener.CANCELLED) {
                    Toast.makeText(this, "Pin Create Cancelled", Toast.LENGTH_SHORT).show();
                }
                refreshActivity();
                break;
            }
            case REQUEST_CODE_CHANGE_PIN : {
                if(resultCode == PinListener.SUCCESS){
                    Intent intent = new Intent(PinLockActivity.this, SetUpPinActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_SET_PIN);
                } else if(resultCode == PinListener.CANCELLED){
                    Toast.makeText(this, "Pin change cancelled", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
                break;
            }
            case REQUEST_CODE_CONFIRM_PIN : {
                if(resultCode == PinListener.SUCCESS){
                    Toast.makeText(this, "Pin Authentication Success", Toast.LENGTH_SHORT).show();
                } else if(resultCode == PinListener.CANCELLED) {
//                    Toast.makeText(this, "Pin confirm cancelled :|", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                }
                break;
            }

        }
    }

    private void refreshActivity() {
        finish();
    }


}
