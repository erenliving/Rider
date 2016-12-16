package com.erenlivingstone.rider.appscreens.searchlocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.networking.BikeShareTorontoAPI;
import com.erenlivingstone.rider.services.UtilityService;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eren-DSK on 09/12/2016.
 */

public class SearchLocationPresenter implements SearchLocationContract.Presenter,
        Callback<Stations> {

    public static final String TAG = SearchLocationPresenter.class.getSimpleName();

    private final SearchLocationContract.View mSearchLocationView;

    BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location locationExtra = intent.getParcelableExtra(UtilityService.EXTRA_LOCATION);
            if (locationExtra != null) {
                mLocation = new LatLng(locationExtra.getLatitude(), locationExtra.getLongitude());
                loadStations();
            }
        }
    };

    private SearchMode mSearchMode;
    private LatLng mLocation;

    public SearchLocationPresenter(SearchLocationContract.View locationView) {
        mSearchLocationView = locationView;
        mSearchLocationView.setPresenter(this);
    }

    @Override
    public void start() {
        // TODO: complete this method if needed
    }

    @Override
    public void onSearchModeButtonPressed(SearchMode searchMode) {
        mSearchMode = searchMode;

        mSearchLocationView.setSelectedSearchButton(searchMode);
        mSearchLocationView.enableLocationButtons();
    }

    @Override
    public void onLocationButtonPressed(LocationMode locationMode) {
        switch (locationMode) {
            case DEVICE:
                // Check fine location permission has been granted
                if (!mSearchLocationView.checkFineLocationPermission()) {
                    // See if user has denied permission in the past
                    if (mSearchLocationView.shouldShowPermissionRationale()) {
                        // Show a snackbar explaining the request instead
                        mSearchLocationView.showLocationPermissionSnackbar();
                    } else {
                        // Otherwise request permission from user
                        mSearchLocationView.requestFineLocationPermission();
                    }
                } else {
                    // Otherwise permission is granted (which is always the case on pre-M devices)
                    fineLocationPermissionGranted();
                }
                break;
            case CUSTOM:
                break;
        }
    }

    /**
     * Run when fine location permission has been granted, starts a Service to run in the
     * background and broadcasts results when Location is found. Also tells the View to display
     * the loading indicator to show activity.
     */
    @Override
    public void fineLocationPermissionGranted() {
        mSearchLocationView.setLoadingIndicator(true);
        mSearchLocationView.setLoadingIndicatorStatus("Getting location...");

        // Start a request for location, wait for local broadcast to receive latest location
        mSearchLocationView.startRequestLocationService();
    }

    /**
     * This method tells the View to display a loading indicator while network activity is
     * occurring, and triggers an HTTP call to GET the latest Stations data from the API.
     */
    private void loadStations() {
        mSearchLocationView.setLoadingIndicatorStatus("Loading Stations...");

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

    //region Retrofit Callback methods

    /**
     * Retrofit Callback method when successful, tells the View to stop the loading indicator and
     * to communicate that the Stations have loaded successfully to the Activity
     *
     * @param call the call associated with this callback
     * @param response the HTTP response
     */
    @Override
    public void onResponse(Call<Stations> call, Response<Stations> response) {
        mSearchLocationView.onSearchReady(mSearchMode, mLocation, response.body());
    }

    /**
     * Retrofit Callback method when error occurred, tells the View to stop the loading indicator
     * and logs an Error
     *
     * @param call the call associated with this callback
     * @param t an object containing info on the error
     */
    @Override
    public void onFailure(Call<Stations> call, Throwable t) {
        Log.e(TAG, t.getLocalizedMessage(), t);
        mSearchLocationView.setLoadingIndicator(false);
    }

    //endregion

}
