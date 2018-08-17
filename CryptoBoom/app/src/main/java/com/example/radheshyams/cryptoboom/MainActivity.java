package com.example.radheshyams.cryptoboom;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.radheshyams.cryptoboom.fragments.UILessFragment;
import com.example.radheshyams.cryptoboom.recyclerview.Divider;
import com.example.radheshyams.cryptoboom.recyclerview.MyCryptoAdapter;
import com.example.radheshyams.cryptoboom.screens.MainScreen;
import com.example.radheshyams.cryptoboom.viewmodel.CryptoViewModel;
import com.radhe.data.models.CoinModel;

import java.util.List;

public class MainActivity extends LocationActivity implements MainScreen {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recView;
    private MyCryptoAdapter mAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CryptoViewModel mViewModel;
    private long mLastFetchedDataTimeStamp;
    private final static int DATA_FETCHING_INTERVAL=10*1000; //10 seconds
    private final Observer<List<CoinModel>> dataObserver = new Observer<List<CoinModel>>() {
        @Override
        public void onChanged(@Nullable List<CoinModel> coinModels) {
            MainActivity.this.updateData(coinModels);
        }
    };

    private final Observer<String> errorObserver = new Observer<String>() {
        @Override
        public void onChanged(@Nullable String errorMsg) {
            MainActivity.this.setError(errorMsg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        mViewModel= ViewModelProviders.of(this).get(CryptoViewModel.class);
        mViewModel.getCoinsMarketData().observe(this, dataObserver);
        mViewModel.getErrorUpdates().observe(this, errorObserver);
        mViewModel.fetchData();
        getSupportFragmentManager().beginTransaction()
                .add(new UILessFragment(),"UILessFragment").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void bindViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        recView = findViewById(R.id.recView);
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (System.currentTimeMillis() - mLastFetchedDataTimeStamp < DATA_FETCHING_INTERVAL) {
                    Log.d(TAG, "\tNot fetching from network because interval didn't reach");
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
                mViewModel.fetchData();
            }
        });
        mAdapter = new MyCryptoAdapter();
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recView.setLayoutManager(lm);
        recView.setAdapter(mAdapter);
        recView.addItemDecoration(new Divider(this));
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recView.smoothScrollToPosition(0);
            }
        });
        fab=findViewById(R.id.fabExit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.finish();
            }
        });
    }

    private void showErrorToast(String error) {
        Toast.makeText(this, "Error:" + error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateData(List<CoinModel> data) {
        mLastFetchedDataTimeStamp=System.currentTimeMillis();
        mAdapter.setItems(data);
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void setError(String msg) {
        showErrorToast(msg);
    }
}