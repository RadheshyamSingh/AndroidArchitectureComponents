package com.example.radheshyams.cryptoboom.screens;

import com.radhe.data.models.CoinModel;

import java.util.List;

public interface MainScreen {
    void updateData(List<CoinModel> list);
    void setError(String msg);
}
