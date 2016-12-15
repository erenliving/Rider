package com.erenlivingstone.rider.appscreens.bikes;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.services.FetchAddressIntentService;
import com.google.android.gms.maps.model.LatLng;

/**
 * A {@link Fragment} subclass containing a CardView with
 * information on the nearest bikes to the search location.
 *
 * Activities that contain this fragment must implement the
 * {@link BikesFragment.OnBikesFragmentInteractionListener} interface
 * to handle interaction events.
 *
 * Use the {@link BikesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BikesFragment extends Fragment implements BikesContract.View {

    public static final String TAG = BikesFragment.class.getSimpleName();

    private BikesContract.Presenter mPresenter;

    public interface OnBikesFragmentInteractionListener {
        void onBikesFragmentInteraction(boolean selectBike);
    }

    private OnBikesFragmentInteractionListener mListener;

    private SwipeDeck mSwipeDeck;

    public BikesFragment() {
        // Required empty public constructor
    }

    public static BikesFragment newInstance() {
        return new BikesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bikes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeDeck = (SwipeDeck) view.findViewById(R.id.swipe_deck);
        // Let the Presenter handle configuring SwipeDeck views
        mSwipeDeck.setAdapter(mPresenter.getSwipeDeckAdapter());
        // Forward events to Presenter
        mSwipeDeck.setCallback((BikesPresenter)mPresenter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnBikesFragmentInteractionListener) {
            mListener = (OnBikesFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnBikesFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //region BikesContract.View methods

    @Override
    public void setPresenter(BikesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void startFetchAddress(BikesPresenter.AddressResultReceiver resultReceiver, LatLng location) {
        Location locationExtra = new Location(LocationManager.PASSIVE_PROVIDER);
        locationExtra.setLatitude(location.latitude);
        locationExtra.setLongitude(location.longitude);

        // Convert the Station location to an address for display
        FetchAddressIntentService.startActionFetchAddress(getContext(), resultReceiver,
                locationExtra);
    }

    @Override
    public void showStationCard(String stationName, String availableBikes, String distance,
                                String location, String lastCommunicationTime) {
//        mStationNameTextView.setText(stationName);
//        mAvailableBikesTextView.setText(availableBikes + " available bikes");
//        mDistanceTextView.setText(distance);
//        mLocationTextView.setText(location);
//        mLastCommunicationTimeTextView.setText("Last communicated: " + lastCommunicationTime);
    }

    @Override
    public void setStationAddressText(String address) {
//        mLocationTextView.setText(address);
    }

    @Override
    public void showAddressSuccessToast() {
        Toast.makeText(getContext(), getString(R.string.address_found), Toast.LENGTH_SHORT).show();
    }

    //endregion

}
