package com.erenlivingstone.rider.appscreens.bikes;

import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.utils.Utils;

/**
 * Created by Eren on 10/12/2016.
 */

public class BikesPresenter implements BikesContract.Presenter {

    private final BikesContract.View mBikesView;

    private Stations mStations;
    private Station mCurrentStation;

    private boolean mFirstLoad = true;

    public BikesPresenter(BikesContract.View bikesView, Stations stations) {
        mBikesView = bikesView;
        mBikesView.setPresenter(this);
        mStations = stations;
    }

    @Override
    public void start() {
        loadStation();
    }

    private void loadStation() {
        if (mFirstLoad) {
            mCurrentStation = findClosestStation(mStations);
            mFirstLoad = false;
        }
        // TODO: Do I need an additional case here if mCurrentStation is null? When would it become null?

        String distance = "5min walk (500m)"; // TODO: calculate the actual distance

        String lastCommunicationTime = Utils.getFormattedTimestampForDisplay(
                mCurrentStation.getLastCommunicationTime());

        mBikesView.showStationCard(mCurrentStation.getStationName(),
                String.valueOf(mCurrentStation.getAvailableBikes()), distance,
                mCurrentStation.getLocation().toString(), lastCommunicationTime);
    }

    private Station findClosestStation(Stations stations) {
        Station closestStation = stations.stationBeanList.get(0);

        // TODO: complete this method

        return closestStation;
    }

    @Override
    public void onAcceptCardSwipe() {
        // TODO: complete this method
    }

    @Override
    public void onRejectCardSwipe() {
        // TODO: complete this method
    }
}
