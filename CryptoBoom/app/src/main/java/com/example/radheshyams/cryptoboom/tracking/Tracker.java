package com.example.radheshyams.cryptoboom.tracking;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.radheshyams.cryptoboom.MainActivity;

import org.json.JSONObject;

import java.util.HashMap;

public class Tracker implements LifecycleObserver{
    private static final String TAG = Tracker.class.getSimpleName();
    private final String TRACKING_URL = "https://httpbin.org/post";
    private final RequestQueue mQueue;
    private final String mOsVersion;
    private Context mCon;

    public Tracker(Context con) {
        mCon=con;
        mOsVersion = Build.VERSION.RELEASE;
        mQueue = Volley.newRequestQueue(con.getApplicationContext());
        ((AppCompatActivity) con).getLifecycle().addObserver(this);
    }

    private StringRequest generateTrackingStringRequest(final String eventName) {
        return new StringRequest(Request.Method.POST, TRACKING_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log.d(TAG, "onResponse() called with: response = [" + response + "]");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                    }
                }) {
            @Override
            public byte[] getBody() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("eventName", eventName);
                params.put("osVersion", mOsVersion);
                return new JSONObject(params).toString().getBytes();
            }
        };
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void trackOnCreate() {
        Log.d(TAG, "trackOnCreate() called");
        mQueue.add(generateTrackingStringRequest("create"));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void trackOnDestroy() {
        Log.d(TAG, "trackOnDestroy() called");
        ((AppCompatActivity)mCon).getLifecycle().removeObserver(this);
        mQueue.add(generateTrackingStringRequest("destroy"));
        Lifecycle.State currentState=((AppCompatActivity)mCon).getLifecycle().getCurrentState();
        mCon=null;

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void trackOnStart() {
        Log.d(TAG, "trackOnStart() called");
        mQueue.add(generateTrackingStringRequest("start"));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void trackOnResume() {
        Log.d(TAG, "trackOnResume() called");
        mQueue.add(generateTrackingStringRequest("resume"));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void trackOnPause() {
        Log.d(TAG, "trackOnPause() called");
        mQueue.add(generateTrackingStringRequest("pause"));

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void trackOnStop() {
        Log.d(TAG, "trackOnStop() called");
        mQueue.add(generateTrackingStringRequest("stop"));

    }

    public void trackLocation(double lat, double lng) {
        Log.d(TAG, "trackLocation() called with: lat = [" + lat + "], lng = [" + lng + "]");
        mQueue.add(generateTrackingStringRequest("location\t" + lat + "-" + lng));

    }
}
