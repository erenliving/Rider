package com.erenlivingstone.rider.appscreens.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.constants.SearchMode;

/**
 * The initial landing screen of the app.
 * A {@link Fragment} subclass containing 2 buttons to
 * choose what to search for (available bike or available parking).
 *
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnSearchModeSelectedListener} interface
 * to handle interaction events.
 *
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public static final String TAG = HomeFragment.class.getSimpleName();

    private OnSearchModeSelectedListener mListener;

    private Button searchBikesButton, searchParkingButton;

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
                onButtonPressed(SearchMode.BIKES);
            }
        });

        searchParkingButton = (Button) view.findViewById(R.id.search_parking_button);
        searchParkingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPressed(SearchMode.PARKING);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchModeSelectedListener) {
            mListener = (OnSearchModeSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSearchModeSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Called when a search button is clicked, it communicates this
     * event to the Activity along with the corresponding SearchMode
     * value to be used in a later part of the app.
     *
     * @param searchMode the SearchMode value of what to look for
     */
    public void onButtonPressed(SearchMode searchMode) {
        if (mListener != null) {
            mListener.onSearchModeSelected(searchMode);
        }
    }

    public interface OnSearchModeSelectedListener {
        void onSearchModeSelected(SearchMode searchMode);
    }

}
