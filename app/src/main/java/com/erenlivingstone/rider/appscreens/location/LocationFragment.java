package com.erenlivingstone.rider.appscreens.location;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.constants.LocationMode;

/**
 * A {@link Fragment} subclass containing 2 buttons to
 * to give a location to use when searching.
 *
 * Activities that contain this fragment must implement the
 * {@link LocationFragment.OnLocationModeSelectedListener} interface
 * to handle interaction events.
 *
 * Use the {@link LocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationFragment extends Fragment {

    public static final String TAG = LocationFragment.class.getSimpleName();

    private OnLocationModeSelectedListener mListener;

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
        if (context instanceof OnLocationModeSelectedListener) {
            mListener = (OnLocationModeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLocationModeSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a search button is clicked, it communicates this
     * event to the Activity along with the corresponding LocationMode
     * value to be used in a later part of the app.
     *
     * @param locationMode the LocationMode value of what location source to use
     */
    public void onButtonPressed(LocationMode locationMode) {
        // Hide the buttons and display the indeterminate progress bar while retrieving location
        myLocationButton.setVisibility(View.GONE);
        enterLocationButton.setVisibility(View.GONE);
        indeterminateProgressBar.setVisibility(View.VISIBLE);

        // Sleep for 5 seconds to simulate getting location
        // TODO: check/ask for Location permissions here and get device location
        final LocationMode finalLocationMode = locationMode;
        Handler sleepFor5 = new Handler();
        sleepFor5.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mListener != null) {
                    mListener.onLocationModeSelected(finalLocationMode);
                }
            }
        }, 2000);
    }

    public interface OnLocationModeSelectedListener {
        void onLocationModeSelected(LocationMode locationMode);
    }
}
