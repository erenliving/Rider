package com.erenlivingstone.rider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.erenlivingstone.rider.appscreens.bikes.BikesFragment;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.google.android.gms.maps.model.LatLng;

public class CardActivity extends AppCompatActivity
        implements BikesFragment.OnBikesFragmentInteractionListener {

    private SearchMode searchMode;
    private LatLng location;
    private Stations stations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();
        searchMode = (SearchMode) intent.getSerializableExtra(MainActivity.EXTRA_SEARCH_MODE);
        location = intent.getParcelableExtra(MainActivity.EXTRA_LOCATION);
        stations.stationBeanList = intent.getParcelableArrayListExtra(MainActivity.EXTRA_STATIONS);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, BikesFragment.newInstance(), BikesFragment.TAG)
                    .commit();
        }
    }

    @Override
    public void onBikesFragmentInteraction(boolean selectBike) {
        // TODO: complete this method
    }
}
