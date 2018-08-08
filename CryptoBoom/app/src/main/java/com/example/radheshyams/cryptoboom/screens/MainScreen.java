package com.example.radheshyams.cryptoboom.screens;

import com.example.radheshyams.cryptoboom.recyclerview.CoinModel;

import java.util.List;

public interface MainScreen {
    void updateData(List<CoinModel> list);
    void setError(String msg);
}
