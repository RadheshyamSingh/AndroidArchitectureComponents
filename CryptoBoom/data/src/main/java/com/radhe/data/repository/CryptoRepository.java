package com.radhe.data.repository;

import android.arch.lifecycle.LiveData;

import com.radhe.data.models.CoinModel;

import java.util.List;

public interface CryptoRepository {
    LiveData<List<CoinModel>> getCryptoCoinsData();
    LiveData<String> getErrorStream();
    LiveData<Double> getTotalMarketCapStream();
    void fetchData();
}
