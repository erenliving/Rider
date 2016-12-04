package com.erenlivingstone.rider;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.erenlivingstone.rider.appscreens.bikes.BikesFragment;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;

public class CardActivity extends AppCompatActivity
        implements BikesFragment.OnBikesFragmentInteractionListener {

    private SearchMode searchMode;
    private LocationMode locationMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Intent intent = getIntent();
        searchMode = (SearchMode) intent.getSerializableExtra(MainActivity.EXTRA_SEARCH_MODE);

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
