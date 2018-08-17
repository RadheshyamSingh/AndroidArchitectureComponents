package com.example.radheshyams.cryptoboom.viewmodel;


import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.radhe.data.models.CoinModel;
import com.radhe.data.repository.CryptoRepository;
import com.radhe.data.repository.CryptoRepositoryImpl;

import java.util.List;

public class CryptoViewModel extends AndroidViewModel {
    private static final String TAG = CryptoViewModel.class.getSimpleName();
    private CryptoRepository mCryptoRepository;
    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mCryptoRepository.getCryptoCoinsData();
    }
    public LiveData<String> getErrorUpdates() {
        return mCryptoRepository.getErrorStream();
    }
    public CryptoViewModel(@NonNull Application application) {
        super(application);
        mCryptoRepository= CryptoRepositoryImpl.create(application);
    }

    @Override
    protected void onCleared() {
        Log.d(TAG, "onCleared() called");
        super.onCleared();
    }

    public void fetchData() {
        mCryptoRepository.fetchData();
    }

    public LiveData<Double>getTotalMarketCap()
    {
        return mCryptoRepository.getTotalMarketCapStream();
    }

}