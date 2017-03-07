package com.example.jirehcordova.tccs_mobilev2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jireh Cordova on 07/03/2017.
 */

public class splashactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, Launcher.class);
        startActivity(intent);
        finish();
    }

}
