package com.erenlivingstone.rider.appscreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.appscreens.home.HomeFragment;
import com.erenlivingstone.rider.appscreens.home.HomePresenter;
import com.erenlivingstone.rider.appscreens.location.LocationFragment;
import com.erenlivingstone.rider.appscreens.location.LocationPresenter;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        HomeFragment.OnHomeFragmentInteractionListener,
        LocationFragment.OnLocationFragmentInteractionListener {

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

        // Initialize the Presenter, it hooks itself to the View during construction
        HomePresenter homePresenter = new HomePresenter(homeFragment);
    }

    private void initializeLocationFragment() {
        locationFragment = LocationFragment.newInstance();

        // Initialize the Presenter, it hooks itself to the View during construction
        LocationPresenter locationPresenter = new LocationPresenter(locationFragment);
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
        this.searchMode = searchMode;
    }

    @Override
    public void onStationsLoaded(Stations stations) {
        this.stations = stations;

        showLocationFragment();
    }

    //endregion

    //region LocationFragment interface methods

    @Override
    public void onLocationFound(LatLng location) {
        startCardActivity(searchMode, location, stations);
    }

    //endregion

    //endregion
}
