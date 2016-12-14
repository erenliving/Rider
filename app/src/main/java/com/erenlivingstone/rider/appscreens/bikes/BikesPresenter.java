package com.erenlivingstone.rider.appscreens.bikes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.Toast;

import com.erenlivingstone.rider.R;
import com.erenlivingstone.rider.constants.Constants;
import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.services.FetchAddressIntentService;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Eren on 10/12/2016.
 */

public class BikesPresenter implements BikesContract.Presenter {

    private final BikesContract.View mBikesView;

    private AddressResultReceiver mResultReceiver;

    private LatLng mLocation;
    private Stations mStations;
    private Station mCurrentStation;

    private boolean mFirstLoad = true;

    public BikesPresenter(BikesContract.View bikesView, LatLng location, Stations stations) {
        mBikesView = bikesView;
        mBikesView.setPresenter(this);
        mResultReceiver = new AddressResultReceiver(new Handler());
        mLocation = location;
        mStations = stations;
    }

    @Override
    public void start() {
        loadStation();
    }

    private void loadStation() {
        // TODO: display a loading indicator

        if (mFirstLoad) {
            mCurrentStation = mStations.stationBeanList.get(0);
            mFirstLoad = false;
        }
        // TODO: Do I need an additional case here if mCurrentStation is null? When would it become null?

        String distance = "5min walk (500m)"; // TODO: calculate the actual distance

        String lastCommunicationTime = Utils.getFormattedTimestampForDisplay(
                mCurrentStation.getLastCommunicationTime());

        mBikesView.startFetchAddress(mResultReceiver, mCurrentStation.getLocation());

        mBikesView.showStationCard(mCurrentStation.getStationName(),
                String.valueOf(mCurrentStation.getAvailableBikes()), distance,
                mCurrentStation.getLocation().toString(), lastCommunicationTime);
    }

    @Override
    public void onAcceptCardSwipe() {
        // TODO: complete this method
    }

    @Override
    public void onRejectCardSwipe() {
        // TODO: complete this method
    }

    public class AddressResultReceiver extends ResultReceiver {

        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // Display the address string
            // or an error message sent from the intent service.
            String address = resultData.getString(Constants.RESULT_DATA_KEY);
            mBikesView.setStationAddressText(address);

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                mBikesView.showAddressSuccessToast();
            }
        }

    }

}
