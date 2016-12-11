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
 * {@link LocationFragment.OnLocationFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment implements LocationContract.View {

    public static final String TAG = LocationFragment.class.getSimpleName();

    private LocationContract.Presenter mPresenter;

    public interface OnLocationFragmentInteractionListener {
        void onLocationFound(LatLng location);
    }

    private OnLocationFragmentInteractionListener mListener;

    private static final int PERMISSION_REQUEST_CODE = 0;

    private Button myLocationButton, enterLocationButton;
    private ProgressBar indeterminateProgressBar;

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
                mPresenter.onLocationButtonPressed(LocationMode.DEVICE, getActivity());
            }
        });

        enterLocationButton = (Button) view.findViewById(R.id.enter_location_button);
        enterLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onLocationButtonPressed(LocationMode.CUSTOM, getActivity());
            }
        });

        indeterminateProgressBar = (ProgressBar) view.findViewById(R.id.indeterminate_progress_bar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationFragmentInteractionListener) {
            mListener = (OnLocationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocationFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Forward broadcasts to the Presenter
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                ((LocationPresenter) mPresenter).locationBroadcastReceiver, UtilityService
                        .getLocationUpdatedIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister broadcasts to the Presenter
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                ((LocationPresenter)mPresenter).locationBroadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //region Permission methods

    /**
     * Permissions request result callback
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mPresenter.fineLocationPermissionGranted(getActivity());
                }
                break;
        }
    }

    //endregion

    //region LocationContract.View methods

    @Override
    public void setPresenter(LocationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Method to display or hide the indeterminate progress bar indicating finding location
     *
     * @param active true to display indicator, false to hide indicator
     */
    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            myLocationButton.setVisibility(View.GONE);
            enterLocationButton.setVisibility(View.GONE);
            indeterminateProgressBar.setVisibility(View.VISIBLE);
        } else {
            myLocationButton.setVisibility(View.VISIBLE);
            enterLocationButton.setVisibility(View.VISIBLE);
            indeterminateProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Request the fine location permission from the user
     */
    @Override
    public void requestFineLocationPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    /**
     * Show a permission explanation snackbar for using Location
     */
    @Override
    public void showLocationPermissionSnackbar() {
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
     * Communicates that the device has found its Location to the Activity
     *
     * @param location the location coordinates of the device
     */
    @Override
    public void onLocationFound(LatLng location) {
        if (mListener != null) {
            mListener.onLocationFound(location);
        }
    }

    //endregion
}
