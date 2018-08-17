package com.radhe.data.repository.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.radhe.data.db.RoomDb;
import com.radhe.data.entity.CryptoCoinEntity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class LocalDataSource implements DataSource<List<CryptoCoinEntity>> {
    private final RoomDb mDb;
    private final MutableLiveData<String> mError=new MutableLiveData<>();
    public LocalDataSource(Context mAppContext) {
        mDb= RoomDb.getDatabase(mAppContext);
    }
    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mDb.coinDao().getAllCoinsLive();
    }
    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void writeData(List<CryptoCoinEntity> coins) {
        try {
            mDb.coinDao().insertCoins(coins);
        }catch(Exception e)
        {
            e.printStackTrace();
            mError.postValue(e.getMessage());
        }
    }

    public List<CryptoCoinEntity> getAllCoins() {
        return mDb.coinDao().getAllCoins();
    }
}
