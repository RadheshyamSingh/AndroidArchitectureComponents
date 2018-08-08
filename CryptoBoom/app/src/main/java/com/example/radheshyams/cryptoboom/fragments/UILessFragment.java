package com.example.radheshyams.cryptoboom.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.example.radheshyams.cryptoboom.viewmodel.CryptoViewModel;

import java.util.Observable;

/**
 * A simple {@link Fragment} subclass.
 */
public class UILessFragment extends Fragment {
    private static final String TAG = UILessFragment.class.getSimpleName();
    private CryptoViewModel mViewModel;
    private final Observer<Double> mObserver= new Observer<Double>() {
        @Override
        public void onChanged(@Nullable Double totalMarketCap) {
            Log.d(TAG, "onChanged() called with: aDouble = [" + totalMarketCap + "]");
        }
    };


    public UILessFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //mViewModel = ViewModelProviders.of(this).get(CryptoViewModel.class);

        mViewModel = ViewModelProviders.of(getActivity()).get(CryptoViewModel.class);

        mViewModel.getTotalMarketCap().observe(this,mObserver);

    }



}
