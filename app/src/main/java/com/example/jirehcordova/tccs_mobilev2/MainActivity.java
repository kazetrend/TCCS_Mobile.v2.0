package com.example.jirehcordova.tccs_mobilev2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jirehcordova.tccs_mobilev2.model.responds;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import static android.widget.Toast.LENGTH_LONG;
import static java.security.AccessController.getContext;


/**
 * Created by Jireh Cordova on 04/03/2017.
 */

public class MainActivity extends Activity implements View.OnClickListener{
    Button btn;
    public static final String KEY_LOGGED_IN = "isloggedin";
    int proc = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clockeractivity);

        final SharedPreferences main = getSharedPreferences("AppPref", 0);
        TextView welcomemsg = (TextView)findViewById(R.id.welcome);

        String name = main.getString("name","");
        welcomemsg.setText("WELCOME, "+name+"!");

        btn = (Button)findViewById(R.id.changeling);
        btn.setText("CLOCK OUT");
        btn.setOnClickListener(this);

        if(main.getBoolean(KEY_LOGGED_IN, false)){
            btn.setText("CLOCK OUT");
        }

    }
    //this handles the clockin and out requests

    private void clockRequest(){
        final MaterialDialog dialog = new MaterialDialog.Builder(this).content("Logging In. Please wait a moment.").progress(true, 0).cancelable(false).build();
        dialog.show();

        final SharedPreferences main = getSharedPreferences("AppPref", 0);
        final SharedPreferences.Editor editor = main.edit();

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
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("response", response.toString());
                Gson gson = new Gson();
                responds resp = gson.fromJson(response.toString(), responds.class);
                if(main.getBoolean(KEY_LOGGED_IN, false)){
                    editor.putBoolean(KEY_LOGGED_IN, false);
                    btn.setText("CLOCK IN");
                }else{
                    editor.putBoolean(KEY_LOGGED_IN, true);
                    btn.setText("CLOCK OUT");
                }
                editor.commit();
                finishAffinity();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                MaterialDialog onError = new MaterialDialog.Builder(MainActivity.this)
                        .title("UNAUTHORIZED")
                        .content("You have already Clocked In/ Out for today. You may do so on your next shift.")
                        .positiveText("Okay")
                        .build();
                onError.show();
                error.printStackTrace();

                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        if(onError.isShowing()){
                            onError.dismiss();
                        }
                        MaterialDialog net = new MaterialDialog.Builder(MainActivity.this)
                                .title("Action Failed")
                                .content("Connection failed. Server unresponsive.")
                                .positiveText("Okay")
                                .build();
                        net.show();
                        // Show timeout error message
                    }
                }
            }
        });
        VolleyHelper.getInstance(this).addToRequestQueue(request);


    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.changeling:
                clockRequest();
                break;
        }
    }
}

