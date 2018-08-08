package com.radhe.data.models;

public class CoinModel {
    public final String name;
    public final String symbol;
    public final int imageUrl;
    public final String priceUsd;
    public final String volume24H;
    public final double marketCap;

    public CoinModel(String name, String symbol, int imageUrl, String priceUsd, String volume24H, double marketCap) {
        this.name = name;
        this.symbol = symbol;
        this.imageUrl = imageUrl;
        this.priceUsd = priceUsd;
        this.volume24H = volume24H;
        this.marketCap = marketCap;
    }
}
