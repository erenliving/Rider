package com.erenlivingstone.rider.appscreens;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.appscreens.bikes.BikesFragment;
import com.erenlivingstone.rider.appscreens.bikes.BikesPresenter;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.google.android.gms.maps.model.LatLng;

public class CardActivity extends AppCompatActivity
        implements BikesFragment.OnBikesFragmentInteractionListener {

    private SearchMode searchMode;
    private LatLng location;
    private Stations stations = new Stations();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();
        searchMode = (SearchMode) intent.getSerializableExtra(MainActivity.EXTRA_SEARCH_MODE);
        location = intent.getParcelableExtra(MainActivity.EXTRA_LOCATION);
        stations.stationBeanList = intent.getParcelableArrayListExtra(MainActivity.EXTRA_STATIONS);

        BikesFragment bikesFragment = (BikesFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (bikesFragment == null) {
            bikesFragment = BikesFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, bikesFragment, BikesFragment.TAG)
                    .commit();

            // Initialize the Presenter, it hooks itself to the View during construction
            BikesPresenter bikesPresenter = new BikesPresenter(bikesFragment, stations);
        }

        if (savedInstanceState != null) {
            // TODO: get the stored current station and set it as the current card to load in the presenter
        }
    }

    @Override
    public void onBikesFragmentInteraction(boolean selectBike) {
        // TODO: complete this method
    }
}
