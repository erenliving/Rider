package com.erenlivingstone.rider.appscreens.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;

/**
 * The initial landing screen of the app.
 * A {@link Fragment} subclass containing 2 buttons to
 * choose what to search for (available bike or available parking).
 *
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnHomeFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements HomeContract.View {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private HomeContract.Presenter mPresenter;

    public interface OnHomeFragmentInteractionListener {
        void onSearchModeSelected(SearchMode searchMode);
        void onStationsLoaded(Stations stations);
    }

    private OnHomeFragmentInteractionListener mListener;

    private Button searchBikesButton, searchParkingButton;
    private ProgressBar indeterminateProgressBar;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchBikesButton = (Button) view.findViewById(R.id.search_bikes_button);
        searchBikesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSearchButtonPressed(SearchMode.BIKES);
            }
        });

        searchParkingButton = (Button) view.findViewById(R.id.search_parking_button);
        searchParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.onSearchButtonPressed(SearchMode.PARKING);
            }
        });

        indeterminateProgressBar = (ProgressBar) view.findViewById(R.id.indeterminate_progress_bar);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnHomeFragmentInteractionListener) {
            mListener = (OnHomeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnHomeFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //region HomeContract.View methods

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Method to display or hide the indeterminate progress bar indicating loading
     *
     * @param active true to display indicator, false to hide indicator
     */
    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            searchBikesButton.setVisibility(View.GONE);
            searchParkingButton.setVisibility(View.GONE);
            indeterminateProgressBar.setVisibility(View.VISIBLE);
        } else {
            searchBikesButton.setVisibility(View.VISIBLE);
            searchParkingButton.setVisibility(View.VISIBLE);
            indeterminateProgressBar.setVisibility(View.GONE);
        }
    }

    /**
     * Communicates the selected SearchMode value to the Activity
     *
     * @param searchMode the selected SearchMode
     */
    @Override
    public void onSearchModeSelected(SearchMode searchMode) {
        if (mListener != null) {
            mListener.onSearchModeSelected(searchMode);
        }
    }

    /**
     * Communicates that the network has finished loading the latest data of Stations to the
     * Activity
     *
     * @param stations the latest Stations data from the BikeShare API
     */
    @Override
    public void onStationsLoaded(Stations stations) {
        if (mListener != null) {
            mListener.onStationsLoaded(stations);
        }
    }

    //endregion

}
