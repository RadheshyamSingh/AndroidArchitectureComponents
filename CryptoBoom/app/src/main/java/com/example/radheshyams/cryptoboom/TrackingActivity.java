package com.example.radheshyams.cryptoboom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.radheshyams.cryptoboom.tracking.Tracker;

public class TrackingActivity extends AppCompatActivity {

    protected Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTracker = new Tracker(this);
    }
}
