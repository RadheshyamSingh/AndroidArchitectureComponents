package com.radhe.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.radhe.data.entity.CryptoCoinEntity;

import java.util.List;

@Dao
public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoins(List<CryptoCoinEntity> coins);

    @Query("SELECT * FROM coins")
    LiveData<List<CryptoCoinEntity>> getAllCoinsLive();

    @Query("SELECT * FROM coins")
    List<CryptoCoinEntity> getAllCoins();

    @Query("SELECT * FROM coins LIMIT :limit")
    LiveData<List<CryptoCoinEntity>>getCoins(int limit);

    @Query("SELECT * FROM coins WHERE symbol=:symbol")
    LiveData<CryptoCoinEntity>getCoin(String symbol);
}
