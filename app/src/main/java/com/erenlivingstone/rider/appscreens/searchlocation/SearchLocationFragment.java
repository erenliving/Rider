package com.erenlivingstone.rider.appscreens.searchlocation;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.animations.ResizeAnimation;
import com.erenlivingstone.rider.constants.Constants;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.services.UtilityService;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;

import static com.erenlivingstone.rider.R.string.dock;

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
    private int mShortAnimationDuration;

    private View searchLocationView, loadingView , middleDivider;
    private TextView wantTextView, whereTextView, loadingStatusTextView;
    private Button rideButton, dockButton, myLocationButton, enterLocationButton;
    private ProgressBar indeterminateProgressBar;

    public SearchLocationFragment() {
        // Required empty public constructor
    }

    public static SearchLocationFragment newInstance() {
        return new SearchLocationFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retrieve and cache the system's default "short" animation time.
        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);
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
        searchLocationView = view.findViewById(R.id.search_location_view);
        loadingView = view.findViewById(R.id.loading_view);
        middleDivider = view.findViewById(R.id.middle_divider);
        wantTextView = (TextView) view.findViewById(R.id.want_text_view);
        whereTextView = (TextView) view.findViewById(R.id.where_text_view);
        loadingStatusTextView = (TextView) view.findViewById(R.id.loading_status_text_view);

        rideButton = (Button) view.findViewById(R.id.ride_button);
        String rideEmoji = String.format(getString(R.string.ride), Utils.getEmojiByUnicode(Constants
                .EMOJI_BICYCLE_UNICODE));
        rideButton.setText(rideEmoji);
        rideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSearchModeButtonPressed(SearchMode.BIKES);
            }
        });

        dockButton = (Button) view.findViewById(R.id.dock_button);
        String dockEmoji = String.format(getString(dock), Utils.getEmojiByUnicode(Constants
                .EMOJI_CHEQUERED_FLAG_UNICODE));
        dockButton.setText(dockEmoji);
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

    private void crossfadeButtonsToLoading() {
        // Set the loading view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        loadingView.setAlpha(0f);
        loadingView.setVisibility(View.VISIBLE);

        // Animate the loading view to 100% opacity, and clear any animation
        // listener set on the view.
        loadingView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the searchLocation view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        searchLocationView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchLocationView.setVisibility(View.GONE);
                    }
                });
    }

    private void crossfadeLoadingToButtons() {
        // Set the searchLocation view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        searchLocationView.setAlpha(0f);
        searchLocationView.setVisibility(View.VISIBLE);

        // Animate the searchLocation view to 100% opacity, and clear any animation
        // listener set on the view.
        searchLocationView.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);
                    }
                });
    }

    //region SearchLocationContract.View methods

    @Override
    public void setPresenter(SearchLocationContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void setSelectedSearchButton(SearchMode searchMode) {
        switch (searchMode) {
            case BIKES:
                rideButton.setSelected(true);
                dockButton.setSelected(false);
                break;
            case PARKING:
                rideButton.setSelected(false);
                dockButton.setSelected(true);
                break;
        }
    }

    @Override
    public void showLocationButtonsAnimation() {
        if (whereTextView.getVisibility() != View.VISIBLE) {
            middleDivider.setVisibility(View.VISIBLE);

            AnimationSet animationSet = new AnimationSet(true);

            ResizeAnimation resizeAnimation = new ResizeAnimation(middleDivider, 0, middleDivider
                    .getWidth(), middleDivider.getHeight(), middleDivider.getHeight());
            resizeAnimation.setDuration(mShortAnimationDuration);
            animationSet.addAnimation(resizeAnimation);

            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            animationSet.addAnimation(alphaAnimation);

            middleDivider.startAnimation(animationSet);

            whereTextView.setVisibility(View.VISIBLE);
            whereTextView.setAlpha(0);
            whereTextView.setY(whereTextView.getY() - whereTextView.getHeight());
            whereTextView.animate()
                    .translationYBy(whereTextView.getHeight())
                    .alpha(1)
                    .setDuration(mShortAnimationDuration);

            myLocationButton.setVisibility(View.VISIBLE);
            myLocationButton.setAlpha(0);
            myLocationButton.setY(myLocationButton.getY() - myLocationButton.getHeight());
            myLocationButton.animate()
                    .translationYBy(myLocationButton.getHeight())
                    .alpha(1)
                    .setDuration(mShortAnimationDuration)
                    .setStartDelay(mShortAnimationDuration);

            enterLocationButton.setVisibility(View.VISIBLE);
            enterLocationButton.setAlpha(0);
            enterLocationButton.setY(enterLocationButton.getY() - enterLocationButton.getHeight());
            enterLocationButton.animate()
                    .translationYBy(enterLocationButton.getHeight())
                    .alpha(1)
                    .setDuration(mShortAnimationDuration)
                    .setStartDelay(mShortAnimationDuration * 2);
        }
    }

    /**
     * Method to display or hide the indeterminate progress bar indicating finding location
     *
     * @param active true to display indicator, false to hide indicator
     */
    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            crossfadeButtonsToLoading();
        } else {
            crossfadeLoadingToButtons();
        }
    }

    @Override
    public void setLoadingStatusForLocation() {
        loadingStatusTextView.setText(getString(R.string.getting_location));
    }

    @Override
    public void setLoadingStatusForStations() {
        loadingStatusTextView.setText(getString(R.string.loading_stations));
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
                    .setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary))
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
