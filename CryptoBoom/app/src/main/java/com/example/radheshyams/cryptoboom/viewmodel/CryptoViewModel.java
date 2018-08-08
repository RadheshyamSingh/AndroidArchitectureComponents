package com.example.radheshyams.cryptoboom.viewmodel;


import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.radheshyams.cryptoboom.MainActivity;
import com.example.radheshyams.cryptoboom.R;
import com.example.radheshyams.cryptoboom.entities.CryptoCoinEntity;
import com.example.radheshyams.cryptoboom.recyclerview.CoinModel;
import com.example.radheshyams.cryptoboom.screens.MainScreen;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.radhe.data.entity.CryptoCoinEntity;
import com.radhe.data.models.CoinModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CryptoViewModel extends ViewModel {
    private static final String TAG = CryptoViewModel.class.getSimpleName();

    private MutableLiveData<List<CoinModel>> mDataApi = new MutableLiveData<>();
    private MutableLiveData<String> mError = new MutableLiveData<>();
    private ExecutorService mExecutor = Executors.newFixedThreadPool(5);
    private Context mContext;

    // setter methods
    public LiveData<List<CoinModel>> getCoinsMarketData() {
        return mDataApi;
    }

    public LiveData<String> getErrorUpdates() {
        return mError;
    }

    public LiveData<Double> getTotalMarketCap() {
        return Transformations.map(mDataApi, new Function<List<CoinModel>, Double>() {
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
    public void setAppContext(Context mAppContext) {
        this.mContext = mAppContext;
        if (mQueue == null)
            mQueue = Volley.newRequestQueue(mAppContext);
        fetchData();
    }

    private final String ENDPOINT_FETCH_CRYPTO_DATA = "https://api.coinmarketcap.com/v1/ticker/?limit=100";
    private RequestQueue mQueue;
    private final ObjectMapper mObjMapper = new ObjectMapper();
    private MainScreen mView;

    private String DATA_FILE_NAME = "crypto.data";


    public CryptoViewModel() {
        Log.d(TAG, "CryptoViewModel created");
    }

    public void bind(MainActivity view) {
        mView = view;
        mContext = view.getApplicationContext();
    }

    public void unbind() {
        mView = null;
        mContext = null;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "CryptoViewModel onCleared called");
    }

    @NonNull
    private List<CoinModel> mapEntityToModel(List<CryptoCoinEntity> datum) {
        final ArrayList<CoinModel> listData = new ArrayList<>();
        CryptoCoinEntity entity;
        for (int i = 0; i < datum.size(); i++) {
            entity = datum.get(i);
            int url = (i % 2 == 0) ? R.drawable.bitcoin : R.drawable.ethereum;
            listData.add(new CoinModel(entity.getName(), entity.getSymbol(), url,entity.getPriceUsd(),
                    entity.get24hVolumeUsd(), Double.valueOf(entity.getMarketCapUsd())));
        }

        return listData;
    }

    public void fetchData() {

        final JsonArrayRequest jsonObjReq =
                new JsonArrayRequest(ENDPOINT_FETCH_CRYPTO_DATA,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, "Thread->" +
                                        Thread.currentThread().getName() + "\tGot some network response");
                                CryptoViewModel.this.writeDataToInternalStorage(response);
                                final ArrayList<CryptoCoinEntity> data = CryptoViewModel.this.parseJSON(response.toString());
                                List<CoinModel> mappedData = CryptoViewModel.this.mapEntityToModel(data);
                                mDataApi.setValue(mappedData);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Thread->" +
                                        Thread.currentThread().getName() + "\tGot network error");
                                mError.setValue(error.toString());
                                mExecutor.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Log.d(TAG, "Thread->" + Thread.currentThread().getName() +
                                                    "\tNot fetching from network because of network error - fetching from disk");
                                            JSONArray data = CryptoViewModel.this.readDataFromStorage();
                                            ArrayList<CryptoCoinEntity> entities = CryptoViewModel.this.parseJSON(data.toString());
                                            List<CoinModel> mappedData = CryptoViewModel.this.mapEntityToModel(entities);
                                            mDataApi.postValue(mappedData);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                            }
                        });
        // Add the request to the RequestQueue.
        mQueue.add(jsonObjReq);
    }

    public ArrayList<CryptoCoinEntity> parseJSON(String jsonStr) {
        ArrayList<CryptoCoinEntity> data = null;

        try {
            data = mObjMapper.readValue(jsonStr, new TypeReference<ArrayList<CryptoCoinEntity>>() {
            });
        } catch (Exception e) {
            if (null != mView) {
                mView.setError(e.getMessage());
            }
            e.printStackTrace();
        }
        return data;
    }

    private void writeDataToInternalStorage(JSONArray data) {
        FileOutputStream fos = null;
        try {
            fos = mContext.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(data.toString().getBytes());
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONArray readDataFromStorage() throws JSONException {
        FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(DATA_FILE_NAME);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader bufferedReader = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray(sb.toString());
    }

}