package com.erenlivingstone.rider.appscreens.searchlocation;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import android.widget.TextView;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.services.UtilityService;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

/**
 * A {@link Fragment} subclass containing 2 buttons to
 * to give a location to use when searching.
 *
 * Activities that contain this fragment must implement the
 * {@link SearchLocationFragment.OnSearchLocationFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link SearchLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchLocationFragment extends Fragment implements SearchLocationContract.View {

    public static final String TAG = SearchLocationFragment.class.getSimpleName();

    private SearchLocationContract.Presenter mPresenter;

    public interface OnSearchLocationFragmentInteractionListener {
        void onSearchReady(SearchMode searchMode, LatLng location, Stations stations);
    }

    private OnSearchLocationFragmentInteractionListener mListener;

    private static final int PERMISSION_REQUEST_CODE = 0;

    private TextView locationLabelTextView;
    private Button rideButton, dockButton, myLocationButton, enterLocationButton;
    private ProgressBar indeterminateProgressBar;

    public SearchLocationFragment() {
        // Required empty public constructor
    }

    public static SearchLocationFragment newInstance() {
        return new SearchLocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locationLabelTextView = (TextView) view.findViewById(R.id.location_label_text_view);
        rideButton = (Button) view.findViewById(R.id.ride_button);
        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSearchModeButtonPressed(SearchMode.BIKES);
            }
        });
        dockButton = (Button) view.findViewById(R.id.dock_button);
        dockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSearchModeButtonPressed(SearchMode.PARKING);
            }
        });
        myLocationButton = (Button) view.findViewById(R.id.my_location_button);
        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onLocationButtonPressed(LocationMode.DEVICE);
            }
        });

        enterLocationButton = (Button) view.findViewById(R.id.enter_location_button);
        enterLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onLocationButtonPressed(LocationMode.CUSTOM);
            }
        });

        indeterminateProgressBar = (ProgressBar) view.findViewById(R.id.indeterminate_progress_bar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchLocationFragmentInteractionListener) {
            mListener = (OnSearchLocationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchLocationFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Forward broadcasts to the Presenter
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                ((SearchLocationPresenter) mPresenter).locationBroadcastReceiver, UtilityService
                        .getLocationUpdatedIntentFilter());
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister broadcasts to the Presenter
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
                ((SearchLocationPresenter)mPresenter).locationBroadcastReceiver);
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
                    mPresenter.fineLocationPermissionGranted();
                }
                break;
        }
    }

    //endregion

    //region SearchLocationContract.View methods

    @Override
    public void setPresenter(SearchLocationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void enableLocationButtons() {
        myLocationButton.setEnabled(true);
        enterLocationButton.setEnabled(true);
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
     * Calls the Utils to check Fine Location permission
     *
     * @return true if permission granted, false otherwise
     */
    @Override
    public boolean checkFineLocationPermission() {
        return Utils.checkFineLocationPermission(getContext());
    }

    /**
     * Checks if user denied Fine Location permission in the past
     *
     * @return true if user denied permission in the past, false otherwise
     */
    @Override
    public boolean shouldShowPermissionRationale() {
        return ActivityCompat.shouldShowRequestPermissionRationale(
                getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
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
     * Starts the Service to get the device's Location and broadcast it back
     */
    @Override
    public void startRequestLocationService() {
        UtilityService.requestLocation(getContext());
    }

    /**
     * Communicates that the device has found its Location, and loaded Stations info to the Activity
     *
     * @param searchMode the selected SearchMode value
     * @param location the location coordinates of the device
     * @param stations the collection of Station info
     */
    @Override
    public void onSearchReady(SearchMode searchMode, LatLng location, Stations stations) {
        if (mListener != null) {
            mListener.onSearchReady(searchMode, location, stations);
        }
    }

    //endregion
}
