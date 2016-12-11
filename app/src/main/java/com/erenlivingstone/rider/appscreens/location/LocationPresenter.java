package com.erenlivingstone.rider.appscreens.location;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.services.UtilityService;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eren-DSK on 09/12/2016.
 */

public class LocationPresenter implements LocationContract.Presenter {

    private final LocationContract.View mLocationView;

    BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location locationExtra = intent.getParcelableExtra(UtilityService.EXTRA_LOCATION);
            if (locationExtra != null) {
                mLocationView.setLoadingIndicator(false);
                LatLng location = new LatLng(locationExtra.getLatitude(), locationExtra.getLongitude());
                mLocationView.onLocationFound(location);
            }
        }
    };

    public LocationPresenter(LocationContract.View locationView) {
        mLocationView = locationView;
        mLocationView.setPresenter(this);
    }

    @Override
    public void start() {
        // TODO: complete this method if needed
    }

    @Override
    public void onLocationButtonPressed(LocationMode locationMode, Activity activity) {
        switch (locationMode) {
            case DEVICE:
                // Check fine location permission has been granted
                if (!Utils.checkFineLocationPermission(activity)) {
                    // See if user has denied permission in the past
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show a snackbar explaining the request instead
                        mLocationView.showLocationPermissionSnackbar();
                    } else {
                        // Otherwise request permission from user
                        mLocationView.requestFineLocationPermission();
                    }
                } else {
                    // Otherwise permission is granted (which is always the case on pre-M devices)
                    fineLocationPermissionGranted(activity);
                }
                break;
            case CUSTOM:
                break;
        }

        // Clear activity to avoid leaks
        activity = null;
    }

    /**
     * Run when fine location permission has been granted, starts a Service to run in the
     * background and broadcasts results when Location is found. Also tells the View to display
     * the loading indicator to show activity.
     *
     * @param activity the Activity context to start the Service with
     */
    @Override
    public void fineLocationPermissionGranted(Activity activity) {
        mLocationView.setLoadingIndicator(true);

        // Start a request for location, wait for local broadcast to receive latest location
        UtilityService.requestLocation(activity);

        // Clear activity to avoid leaks
        activity = null;
    }

}
