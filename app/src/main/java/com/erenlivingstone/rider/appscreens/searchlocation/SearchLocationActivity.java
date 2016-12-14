package com.erenlivingstone.rider.appscreens.searchlocation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.appscreens.CardActivity;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SearchLocationActivity extends AppCompatActivity implements
        SearchLocationFragment.OnSearchLocationFragmentInteractionListener {

    public static final String TAG = SearchLocationActivity.class.getSimpleName();

    public static final String EXTRA_SEARCH_MODE = "com.eren.rider.SEARCH_MODE";
    public static final String EXTRA_LOCATION = "com.eren.rider.LOCATION";
    public static final String EXTRA_STATIONS = "com.eren.rider.STATIONS";

    //region Lifecycle methods

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SearchLocationFragment searchLocationFragment = (SearchLocationFragment) getSupportFragmentManager().findFragmentById(R.id.container);
        if (searchLocationFragment == null) {
            searchLocationFragment = SearchLocationFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, searchLocationFragment, SearchLocationFragment.TAG)
                    .commit();

            // Initialize the Presenter, it hooks itself to the View during construction
            SearchLocationPresenter searchLocationPresenter = new SearchLocationPresenter(searchLocationFragment);
        }

        if (savedInstanceState != null) {
            // TODO: get the stored search mode if selected and set it in the Presenter
        }
    }

    //endregion

    //region Navigation methods

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

    //region SearchLocationFragment interface methods

    @Override
    public void onSearchReady(SearchMode searchMode, LatLng location, Stations stations) {
        startCardActivity(searchMode, location, stations);
    }

    //endregion

    //endregion
}
