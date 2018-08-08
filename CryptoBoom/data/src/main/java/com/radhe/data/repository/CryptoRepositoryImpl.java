package com.radhe.data.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.radhe.data.entity.CryptoCoinEntity;
import com.radhe.data.mapper.CryptoMapper;
import com.radhe.data.models.CoinModel;
import com.radhe.data.repository.datasource.LocalDataSource;
import com.radhe.data.repository.datasource.RemoteDataSource;

import org.json.JSONArray;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CryptoRepositoryImpl implements CryptoRepository {
    private static final String TAG = CryptoRepositoryImpl.class.getSimpleName();
    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private final RemoteDataSource mRemoteDataSource;
    private final LocalDataSource mLocalDataSource;
    private final CryptoMapper mMapper;
    MediatorLiveData<List<CoinModel>> mDataMerger = new MediatorLiveData<>();
    MediatorLiveData<String> mErrorMerger = new MediatorLiveData<>();

    private CryptoRepositoryImpl(RemoteDataSource mRemoteDataSource, LocalDataSource mLocalDataSource, CryptoMapper mapper) {
        this.mRemoteDataSource = mRemoteDataSource;
        this.mLocalDataSource = mLocalDataSource;
        mMapper = mapper;
        mDataMerger.addSource(this.mRemoteDataSource.getDataStream(), entities ->
                mExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        mLocalDataSource.writeData(mMapper.mapEntitiesToString(entities));
                        List<CoinModel> list = mMapper.mapEntityToModel(entities);
                        mDataMerger.postValue(list);

                    }
                })
        );
        mDataMerger.addSource(this.mLocalDataSource.getDataStream(), new Observer<JSONArray>() {
                    @Override
                    public void onChanged(@Nullable JSONArray json) {
                        mExecutor.execute(new Runnable() {
                            @Override
                            public void run() {
                                List<CryptoCoinEntity> entities = mMapper.mapJSONToEntity(json.toString());
                                List<CoinModel> models = mMapper.mapEntityToModel(entities);
                                mDataMerger.postValue(models);
                            }
                        });
                    }
                }

        );
        mErrorMerger.addSource(mRemoteDataSource.getErrorStream(), new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String errorStr) {
                        mErrorMerger.setValue(errorStr);
                        Log.d(TAG, "Network error -> fetching from LocalDataSource");
                        mLocalDataSource.fetch();
                    }
                }
        );
        mErrorMerger.addSource(mLocalDataSource.getErrorStream(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String errorStr) {
                mErrorMerger.setValue(errorStr);
            }
        });
    }

    public static CryptoRepository create(Context mAppContext) {
        final CryptoMapper mapper = new CryptoMapper();
        final RemoteDataSource remoteDataSource = new RemoteDataSource(mAppContext, mapper);
        final LocalDataSource localDataSource = new LocalDataSource(mAppContext);
        return new CryptoRepositoryImpl(remoteDataSource, localDataSource, mapper);
    }

    @Override
    public void fetchData() {
        mRemoteDataSource.fetch();
    }

    @Override
    public LiveData<List<CoinModel>> getCryptoCoinsData() {
        return mDataMerger;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mErrorMerger;
    }

    @Override
    public LiveData<Double> getTotalMarketCapStream() {
        return Transformations.map(mDataMerger, new Function<List<CoinModel>, Double>() {
            @Override
            public Double apply(List<CoinModel> input) {
                double totalMarketCap = 0;
                for (int i = 0; i < input.size(); i++) {
                    totalMarketCap += input.get(i).marketCap;
                }
                return totalMarketCap;
            }
        });
    }
}
