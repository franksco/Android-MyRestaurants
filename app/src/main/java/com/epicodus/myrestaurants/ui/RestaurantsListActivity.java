package com.epicodus.myrestaurants.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.epicodus.myrestaurants.Constants;
import com.epicodus.myrestaurants.R;
import com.epicodus.myrestaurants.adapters.RestaurantListAdapter;
import com.epicodus.myrestaurants.models.Restaurant;
import com.epicodus.myrestaurants.services.YelpService;

import okhttp3.Call;
import okhttp3.Callback;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Response;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RestaurantsListActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private String mRecentAddress;

    @Bind(R.id.recyclerView) RecyclerView mRecyclerView;
    private RestaurantListAdapter mAdapter;

    private int PERMISSION_ACCESS_INTERNET = 0;
    public static final String TAG = RestaurantsListActivity.class.getSimpleName();

    public ArrayList<Restaurant> mRestaurants = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String location = intent.getStringExtra("location");

        getRestaurants(location);

//        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
//
//
//        if (mRecentAddress != null) {
//            getRestaurants(mRecentAddress);
//        }




    }

    private void getRestaurants(String location) {
        final YelpService yelpService = new YelpService();
        yelpService.findRestaurants(location, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                mRestaurants = yelpService.processResults(response);

                RestaurantsListActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mAdapter = new RestaurantListAdapter(getApplicationContext(), mRestaurants);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager =
                                new LinearLayoutManager(RestaurantsListActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);
                    }

                });
            }

        });
    }
}