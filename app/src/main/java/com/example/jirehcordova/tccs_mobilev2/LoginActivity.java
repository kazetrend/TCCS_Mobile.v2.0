package com.example.jirehcordova.tccs_mobilev2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jirehcordova.tccs_mobilev2.model.User;
import com.google.gson.Gson;
import com.manusunny.pinlock.PinListener;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jireh Cordova on 04/03/2017.
 */

public class LoginActivity extends Activity {

    public static final int REQUEST_CODE_SET_PIN = 0;

    static SharedPreferences pinLockPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity);
        pinLockPrefs = getSharedPreferences("AppPref", MODE_PRIVATE);

        final EditText email = (EditText)findViewById(R.id.emailinput);
        final EditText defPin = (EditText)findViewById(R.id.defaultpin);

//      defPin.setFilters(new InputFilter[]{});
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4); //Filter to 10 characters
        defPin.setFilters(filters);

        Button setPin = (Button)findViewById(R.id.setPin);
        final Button submit = (Button)findViewById(R.id.submit);

        submit.setEnabled(false);

        final RelativeLayout kai = (RelativeLayout)findViewById(R.id.kai);
        setPin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View button){
                kai.removeView(button);
                Intent intent = new Intent(LoginActivity.this,SetUpPinActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SET_PIN);
                submit.setEnabled(true);
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String newPin = pinLockPrefs.getString("pinCode", "");
                pinLockPrefs.edit();
                loginRequest(email.getText().toString(), defPin.getText().toString(), newPin);
            }
        });
    }
    private void loginRequest(final String email, String pin, String newPin){

        final MaterialDialog dialog = new MaterialDialog.Builder(this).content("Logging In. Please wait a moment.").progress(true, 0).cancelable(false).build();
        dialog.show();
        final SharedPreferences secure = getSharedPreferences("prefs", MODE_PRIVATE);
        final SharedPreferences.Editor set = secure.edit();

        JSONObject body = new JSONObject();
        try{
            body.put("email", email);
            body.put("pinCode", pin);
            body.put("newPin", newPin);}
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.44:8080/api/v1/login", body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Log.d("response", response.toString());
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(), User.class);
                String namee = user.getFirstname();
                String eemail = user.getemail();
                SharedPreferences.Editor editor = pinLockPrefs.edit();
                editor.putString("name", namee);
                editor.putString("eemail", eemail);
                editor.commit();
                set.putBoolean("firstRun", false);
                set.commit();

                Log.d("check1", pinLockPrefs.getString("name", ""));

                Boolean firstlogin = user.getisFirstlogin();

                Intent intent = new Intent (LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                MaterialDialog onError = new MaterialDialog.Builder(LoginActivity.this)
                        .title("UNAUTHORIZED")
                        .content("You are not allowed to Login as of this moment. Contact Supervisor.")
                        .positiveText("Okay")
                        .build();
                onError.show();
                error.printStackTrace();

                if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        if(onError.isShowing()){
                            onError.dismiss();
                        }
                        MaterialDialog net = new MaterialDialog.Builder(LoginActivity.this)
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SET_PIN: {
                if (resultCode == PinListener.SUCCESS) {
                    Toast.makeText(this, "Pin created successfully", Toast.LENGTH_SHORT).show();

                } else if (resultCode == PinListener.CANCELLED) {
                    Toast.makeText(this, "Pin set canceled", Toast.LENGTH_SHORT).show();
                }
                //refreshActivity();
                break;
            }
        }
    }
}


