package com.radhe.data.repository.datasource;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.radhe.data.entity.CryptoCoinEntity;
import com.radhe.data.mapper.CryptoMapper;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class RemoteDataSource implements DataSource<List<CryptoCoinEntity>>{
    private static final String TAG = RemoteDataSource.class.getSimpleName();
    public final String ENDPOINT_FETCH_CRYPTO_DATA = "https://api.coinmarketcap.com/v1/ticker/?limit=100";
    private final RequestQueue mQueue;
    private final CryptoMapper mObjMapper;
    private final MutableLiveData<String> mError=new MutableLiveData<>();
    private final MutableLiveData<List<CryptoCoinEntity>> mDataApi=new MutableLiveData<>();

    public RemoteDataSource(Context context, CryptoMapper objMapper){
        mQueue = Volley.newRequestQueue(context);
        mObjMapper=objMapper;
    }

    @Override
    public LiveData<List<CryptoCoinEntity>> getDataStream() {
        return mDataApi;
    }

    @Override
    public LiveData<String> getErrorStream() {
        return mError;
    }

    public void fetch() {
        final JsonArrayRequest jsonObjReq =
                new JsonArrayRequest(ENDPOINT_FETCH_CRYPTO_DATA,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, "Thread->" +
                                        Thread.currentThread().getName() + "\tGot some network response");
                                final ArrayList<CryptoCoinEntity> data = mObjMapper.mapJSONToEntity(response.toString());
                                mDataApi.setValue(data);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(TAG, "Thread->" +
                                        Thread.currentThread().getName() + "\tGot network error");
                                mError.setValue(error.toString());
                            }
                        });
        mQueue.add(jsonObjReq);
    }
}
