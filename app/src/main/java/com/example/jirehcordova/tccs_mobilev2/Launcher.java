package com.example.jirehcordova.tccs_mobilev2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.manusunny.pinlock.PinListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jireh Cordova on 05/03/2017.
 */

public class Launcher extends Activity {

    public static final int REQUEST_CODE_CONFIRM_PIN = 2;
    final SharedPreferences settings = getSharedPreferences("prefs", 0);
    final boolean firstRun = settings.getBoolean("firstRun", true);
    final boolean waitRequest = settings.getBoolean("request", false);
    final SharedPreferences.Editor editor = settings.edit();

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        if(firstRun){
            /*editor.putBoolean("firstRun", false);
            editor.commit();*/
            Intent i = new Intent(Launcher.this, LoginActivity.class);
            startActivity(i);
        }else if(!firstRun&&!waitRequest){
            Intent intent = new Intent(Launcher.this, ConfirmPin.class);
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_PIN);
        }else if(waitRequest){
            Intent intent = new Intent(Launcher.this, PinForgot.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_CONFIRM_PIN : {
                if(resultCode == PinListener.SUCCESS){
                    Toast.makeText(this, "Pin accepted", Toast.LENGTH_SHORT).show();
                    Intent k = new Intent(Launcher.this, MainActivity.class);
                    startActivity(k);
                }
                else if(resultCode == PinListener.CANCELLED)
                {
                    finishAffinity();
                }
                else if(resultCode == PinListener.FORGOT)
                {
                    PinChangeRequest();
                }
                break;
            }
        }
    }

    private void PinChangeRequest(){
        final MaterialDialog dialog = new MaterialDialog.Builder(this).content("Sending request. Please wait a moment").progress(true, 0).cancelable(false).build();
        dialog.show();

        final SharedPreferences main = getSharedPreferences("AppPref", 0);

        String email = main.getString("eemail", "");

        JSONObject body = new JSONObject();

        try{
            body.put("email", email);}
        catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.44:8080/api/v1/clock", body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("response", response.toString());
                editor.putBoolean("request", true);
                Intent intent = new Intent(Launcher.this, PinForgot.class);
                startActivity(intent);
                }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Launcher.this, "Request failed", Toast.LENGTH_LONG).show();
            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }
}
