package com.erenlivingstone.rider.appscreens.location;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.services.UtilityService;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.model.LatLng;

/**
 * A {@link Fragment} subclass containing 2 buttons to
 * to give a location to use when searching.
 *
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnLocationInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {

    public static final String TAG = LocationFragment.class.getSimpleName();

    private static final int PERMISSION_REQUEST_CODE = 0;

    private OnLocationInteractionListener mListener;

    private Button myLocationButton, enterLocationButton;
    private ProgressBar indeterminateProgressBar;

    private BroadcastReceiver locationBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(UtilityService.EXTRA_LOCATION);
            if (location != null) {
                LatLng latestLocation = new LatLng(location.getLatitude(), location.getLongitude());
                if (mListener != null) {
                    mListener.onLocationFound(latestLocation);
                }
            }
        }
    };

    public LocationFragment() {
        // Required empty public constructor
    }

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myLocationButton = (Button) view.findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(LocationMode.DEVICE);
            }
        });

        enterLocationButton = (Button) view.findViewById(R.id.enter_location_button);
        enterLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(LocationMode.CUSTOM);
            }
        });

        indeterminateProgressBar = (ProgressBar) view.findViewById(R.id.indeterminate_progress_bar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationInteractionListener) {
            mListener = (OnLocationInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocationInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                locationBroadcastReceiver, UtilityService.getLocationUpdatedIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(locationBroadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //region Event methods

    /**
     * Called when a search button is clicked, it communicates this
     * event to the Activity along with the corresponding LocationMode
     * value to be used in a later part of the app.
     *
     * @param locationMode the LocationMode value of what location source to use
     */
    public void onButtonPressed(LocationMode locationMode) {
        switch (locationMode) {
            case DEVICE:
                // Check fine location permission has been granted
                if (!Utils.checkFineLocationPermission(getContext())) {
                    // See if user has denied permission in the past
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show a snackbar explaining the request instead
                        showLocationPermissionSnackbar();
                    } else {
                        // Otherwise request permission from user
                        requestFineLocationPermission();
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

    //endregion

    //region Permission methods

    /**
     * Request the fine location permission from the user
     */
    private void requestFineLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    /**
     * Run when fine location permission has been granted, starts a Service to run in the
     * background and broadcasts results when Location is found. Also displays an indeterminate
     * ProgressBar to show activity.
     */
    private void fineLocationPermissionGranted() {
        showIndeterminateProgressBar();

        // Start a request for location, wait for local broadcast to receive latest location
        UtilityService.requestLocation(getActivity());
    }

    /**
     * Show a permission explanation snackbar for using Location
     */
    private void showLocationPermissionSnackbar() {
        View view = getView();
        if (view != null) {
            Snackbar.make(view,
                    R.string.location_permission_explanation, Snackbar.LENGTH_LONG)
                    .setAction(R.string.location_permission_explanation_action, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestFineLocationPermission();
                        }
                    })
                    .show();
        }
    }

    /**
     * Permissions request result callback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fineLocationPermissionGranted();
                }
                break;
        }
    }

    //endregion

    /**
     * Hides the buttons and displays the indeterminate progress bar
     */
    public void showIndeterminateProgressBar() {
        myLocationButton.setVisibility(View.GONE);
        enterLocationButton.setVisibility(View.GONE);
        indeterminateProgressBar.setVisibility(View.VISIBLE);
    }

    public interface OnLocationInteractionListener {
        void onLocationFound(LatLng location);
    }
}
