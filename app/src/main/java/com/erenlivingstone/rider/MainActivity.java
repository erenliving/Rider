package com.erenlivingstone.rider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.erenlivingstone.rider.appscreens.home.HomeFragment;
import com.erenlivingstone.rider.appscreens.location.LocationFragment;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.networking.BikeShareTorontoAPI;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnSearchModeSelectedListener,
        LocationFragment.OnLocationInteractionListener,
        Callback<Stations> {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_SEARCH_MODE = "com.eren.rider.SEARCH_MODE";
    public static final String EXTRA_LOCATION = "com.eren.rider.LOCATION";
    public static final String EXTRA_STATIONS = "com.eren.rider.STATIONS";

    private HomeFragment homeFragment;
    private LocationFragment locationFragment;

    private SearchMode searchMode;

    private Stations stations;

    //region Lifecycle methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeHomeFragment();
        initializeLocationFragment();

        showHomeFragment();
    }

    //endregion

    //region Fragment instance initialization

    private void initializeHomeFragment() {
        homeFragment = HomeFragment.newInstance();
    }

    private void initializeLocationFragment() {
        locationFragment = LocationFragment.newInstance();
    }

    //endregion

    //region Navigation methods

    private void showHomeFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, homeFragment, HomeFragment.TAG)
                .commit();
    }

    private void showLocationFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, locationFragment, LocationFragment.TAG)
                .commit();
    }

    private void startCardActivity(SearchMode searchMode, LatLng location, Stations stations) {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra(EXTRA_SEARCH_MODE, searchMode);
        intent.putExtra(EXTRA_LOCATION, location);
        // Convert List<Station> into ArrayList<Station> so it can be passed as Parcelable
        intent.putParcelableArrayListExtra(EXTRA_STATIONS, new ArrayList<>(stations.stationBeanList));
        startActivity(intent);
    }

    //endregion

    //region Interface method implementations

    //region HomeFragment interface methods

    @Override
    public void onSearchModeSelected(SearchMode searchMode) {
        // Save the selected SearchMode for use later
        this.searchMode = searchMode;

        // Build and queue a GET request to get the latest data from the BikeShare API
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BikeShareTorontoAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        BikeShareTorontoAPI bikeShareTorontoAPI = retrofit.create(BikeShareTorontoAPI.class);
        Call<Stations> call = bikeShareTorontoAPI.loadStations();
        // Asynchronous call to get data, calls back to this class
        call.enqueue(this);
    }

    //endregion

    //region LocationFragment interface methods

    @Override
    public void onLocationFound(LatLng location) {
        startCardActivity(searchMode, location, stations);
    }

    //endregion

    //region Retrofit Callback methods

    @Override
    public void onResponse(Call<Stations> call, Response<Stations> response) {
        stations = response.body();
        // TODO: store this response's result

        // Navigate to next screen now that latest data has been fetched, based on search selection
        switch (searchMode) {
            case BIKES:
                showLocationFragment();
                break;
            case PARKING:
                // TODO: complete this case
                break;
        }
    }

    @Override
    public void onFailure(Call<Stations> call, Throwable t) {
        Log.e(TAG, t.getLocalizedMessage(), t);

        // Display error message to user and navigate back to start so they aren't stuck
        Toast.makeText(this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        showHomeFragment();
    }

    //endregion

    //endregion
}
