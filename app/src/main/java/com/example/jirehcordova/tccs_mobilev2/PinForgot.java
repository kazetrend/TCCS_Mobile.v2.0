package com.example.jirehcordova.tccs_mobilev2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.jirehcordova.tccs_mobilev2.model.User;
import com.google.gson.Gson;
import com.manusunny.pinlock.PinListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.jirehcordova.tccs_mobilev2.LoginActivity.REQUEST_CODE_SET_PIN;
import static com.example.jirehcordova.tccs_mobilev2.LoginActivity.pinLockPrefs;
import static com.example.jirehcordova.tccs_mobilev2.R.id.setPin;

public class PinForgot extends Activity {
    //final SharedPreferences app = getSharedPreferences("AppPref", MODE_PRIVATE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_forgot);

        final SharedPreferences prefs = getSharedPreferences("AppPref", MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();

        final Button submit = (Button)findViewById(R.id.submit);
        final EditText defPin = (EditText)findViewById(R.id.defaultpin);
        final Button setPin = (Button)findViewById(R.id.setPin);

        submit.setEnabled(false);

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(4); //Filter to 10 characters
        defPin.setFilters(filters);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String newPin = prefs.getString("pinCode", "");
                String email = prefs.getString("eemail", "");
                LoginRequest(email, defPin.getText().toString(), newPin);
            }
        });

        setPin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View button){
                Intent intent = new Intent(PinForgot.this,SetUpPinActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SET_PIN);
                submit.setEnabled(true);
            }
        });
    }
    //handles the forgot pin request
    private void LoginRequest(final String email, String pin, String newPin){
        final MaterialDialog dialog = new MaterialDialog.Builder(this).content("Logging In. Please wait a moment.").progress(true, 0).cancelable(false).build();
        dialog.show();
        SharedPreferences secure = getSharedPreferences("prefs", MODE_PRIVATE);
        final SharedPreferences.Editor set = secure.edit();

        JSONObject body = new JSONObject();
        try{
            body.put("email", email);
            body.put("pinCode", pin);
            body.put("newPin", newPin);}
        catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, "http://192.168.43.44:8080/api/v1/changePin", body, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                Toast.makeText(PinForgot.this, "Pin reset successful. Please remember your pin next time", Toast.LENGTH_LONG).show();

                Intent intent = new Intent (PinForgot.this, MainActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // internal server errors and 400s
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
                new MaterialDialog.Builder(PinForgot.this)
                        .title("ERROR")
                        .content("An error occurred while processing your request. Please try again or contact your admin")
                        .positiveText("Okay")
                        .show();
                error.printStackTrace();
            }
        });

        VolleyHelper.getInstance(this).addToRequestQueue(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_SET_PIN: {
                if (resultCode == PinListener.SUCCESS) {
                    Toast.makeText(this, "Pin accepted", Toast.LENGTH_SHORT).show();
                } else if (resultCode == PinListener.CANCELLED) {
                }
                //refreshActivity();
                break;
            }
        }
    }

}
