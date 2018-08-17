package com.radhe.data.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by omrierez on 18.08.17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)

@Entity(tableName = "coins",
        indices = {@Index("symbol"),
                @Index("total_supply"),
                @Index({"id","symbol"})})

public class CryptoCoinEntity {
    //We are going to get a list of these entities from our api call - this entity is immutable
    @JsonProperty("id")
    @ColumnInfo(name="id")
    private String id;

    @JsonProperty("name")
    @ColumnInfo(name="n")
    private String name;

    @JsonProperty("symbol")
    @PrimaryKey
    @NonNull
    private String symbol;

    @JsonProperty("rank")
    @ColumnInfo(name="rank")
    private String rank;

    @JsonProperty("price_usd")
    private String priceUsd;
    @JsonProperty("price_btc")
    private String priceBtc;
    @JsonProperty("24h_volume_usd")
    private String _24hVolumeUsd;
    @JsonProperty("market_cap_usd")
    private String marketCapUsd;
    @JsonProperty("available_supply")
    private String availableSupply;

    @JsonProperty("total_supply")
    @ColumnInfo(name="total_supply")
    private String totalSupply;

    @ColumnInfo(name="percent_change_1h")
    @JsonProperty("percent_change_1h")
    private String percentChange1h;

    @JsonProperty("percent_change_24h")
    private String percentChange24h;

    @JsonProperty("percent_change_7d")
    @Ignore
    private String percentChange7d;

    @JsonProperty("last_updated")
    @Ignore
    private String lastUpdated;
    @JsonIgnore

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("rank")
    public String getRank() {
        return rank;
    }

    @JsonProperty("rank")
    public void setRank(String rank) {
        this.rank = rank;
    }

    @JsonProperty("price_usd")
    public String getPriceUsd() {
        return priceUsd;
    }

    @JsonProperty("price_usd")
    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    @JsonProperty("price_btc")
    public String getPriceBtc() {
        return priceBtc;
    }

    @JsonProperty("price_btc")
    public void setPriceBtc(String priceBtc) {
        this.priceBtc = priceBtc;
    }

    @JsonProperty("24h_volume_usd")
    public String get24hVolumeUsd() {
        return _24hVolumeUsd;
    }

    @JsonProperty("24h_volume_usd")
    public void set24hVolumeUsd(String _24hVolumeUsd) {
        this._24hVolumeUsd = _24hVolumeUsd;
    }

    @JsonProperty("market_cap_usd")
    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    @JsonProperty("market_cap_usd")
    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    @JsonProperty("available_supply")
    public String getAvailableSupply() {
        return availableSupply;
    }

    @JsonProperty("available_supply")
    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    @JsonProperty("total_supply")
    public String getTotalSupply() {
        return totalSupply;
    }

    @JsonProperty("total_supply")
    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    @JsonProperty("percent_change_1h")
    public String getPercentChange1h() {
        return percentChange1h;
    }

    @JsonProperty("percent_change_1h")
    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    @JsonProperty("percent_change_24h")
    public String getPercentChange24h() {
        return percentChange24h;
    }

    @JsonProperty("percent_change_24h")
    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    @JsonProperty("percent_change_7d")
    public String getPercentChange7d() {
        return percentChange7d;
    }

    @JsonProperty("percent_change_7d")
    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    @JsonProperty("last_updated")
    public String getLastUpdated() {
        return lastUpdated;
    }

    @JsonProperty("last_updated")
    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "CryptoCoinEntity{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", rank='" + rank + '\'' +
                ", priceUsd='" + priceUsd + '\'' +
                ", priceBtc='" + priceBtc + '\'' +
                ", _24hVolumeUsd='" + _24hVolumeUsd + '\'' +
                ", marketCapUsd='" + marketCapUsd + '\'' +
                ", availableSupply='" + availableSupply + '\'' +
                ", totalSupply='" + totalSupply + '\'' +
                ", percentChange1h='" + percentChange1h + '\'' +
                ", percentChange24h='" + percentChange24h + '\'' +
                ", percentChange7d='" + percentChange7d + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}';
    }
}
