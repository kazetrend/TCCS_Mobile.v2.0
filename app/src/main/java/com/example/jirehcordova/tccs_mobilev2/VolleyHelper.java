package com.example.jirehcordova.tccs_mobilev2;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jireh Cordova on 26/02/2017.
 */
public class VolleyHelper {
    private static VolleyHelper ourInstance;
    private Context context;
    private RequestQueue requestQueue;

    public static VolleyHelper getInstance(Context context) {

        if (ourInstance == null) {
            ourInstance = new VolleyHelper(context);
        }

        return ourInstance;
    }

    private VolleyHelper(Context context) {
        this.context = context;

        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request request) {
        request.setRetryPolicy(new DefaultRetryPolicy(10000, 3, 1f));
        getRequestQueue().add(request);
    }
}
