package com.radhe.data.mapper;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radhe.data.R;
import com.radhe.data.entity.CryptoCoinEntity;
import com.radhe.data.models.CoinModel;

import java.util.ArrayList;
import java.util.List;

public class CryptoMapper extends ObjectMapper {
    private final String CRYPTO_URL_PATH = "https://files.coinmarketcap.com/static/img/coins/128x128/%s.png";

    public ArrayList<CryptoCoinEntity> mapJSONToEntity(String jsonStr) {
        ArrayList<CryptoCoinEntity> data = null;

        try {
            data = readValue(jsonStr, new TypeReference<ArrayList<CryptoCoinEntity>>() {
            });
        } catch (Exception e) {
        }
        return data;
    }

    @NonNull
    public List<CoinModel> mapEntityToModel(List<CryptoCoinEntity> datum) {
        final ArrayList<CoinModel> listData = new ArrayList<>();
        CryptoCoinEntity entity;
        for (int i = 0; i < datum.size(); i++) {
            entity = datum.get(i);
            listData.add(new CoinModel(entity.getName(), entity.getSymbol(),
                    String.format(CRYPTO_URL_PATH, entity.getId()),entity.getPriceUsd(),
                    entity.get24hVolumeUsd(), Double.valueOf(entity.getMarketCapUsd())));
        }

        return listData;
    }

    public String mapEntitiesToString(List<CryptoCoinEntity> data)
    {
        try {
            return writeValueAsString(data);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
