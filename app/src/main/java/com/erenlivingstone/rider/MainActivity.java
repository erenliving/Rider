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
import android.view.View;

import com.erenlivingstone.rider.appscreens.home.HomeFragment;
import com.erenlivingstone.rider.appscreens.location.LocationFragment;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnSearchModeSelectedListener,
        LocationFragment.OnLocationInteractionListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final String EXTRA_SEARCH_MODE = "com.eren.rider.SEARCH_MODE";

    private HomeFragment homeFragment;
    private LocationFragment locationFragment;

    private SearchMode searchMode;

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

    private void startCardActivity() {
        Intent intent = new Intent(this, CardActivity.class);
        intent.putExtra(EXTRA_SEARCH_MODE, searchMode);
        startActivity(intent);
    }

    //endregion

    //region Interface method implementations

    @Override
    public void onSearchModeSelected(SearchMode searchMode) {
        this.searchMode = searchMode;
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
    public void onLocationModeSelected(LocationMode locationMode) {
        // TODO: complete this method
    }

    @Override
    public void onLocationFound(LatLng location) {
        startCardActivity();
    }

    //endregion
}
