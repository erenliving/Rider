package com.erenlivingstone.rider.appscreens.bikes;

import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

/**
 * Created by Eren on 10/12/2016.
 */

public class BikesPresenter implements BikesContract.Presenter {

    private final BikesContract.View mBikesView;

    private LatLng mLocation;
    private Stations mStations;
    private Station mCurrentStation;

    private boolean mFirstLoad = true;

    public BikesPresenter(BikesContract.View bikesView, LatLng location, Stations stations) {
        mBikesView = bikesView;
        mBikesView.setPresenter(this);
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
            mCurrentStation = findClosestStation(mLocation, mStations);
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

    /**
     * Loops through each Station in the given collection, calculating the spherical distance
     * between its location and the given location, searching for the closest one.
     *
     * @param location the location of the device or user-entered location
     * @param stations the collection of Stations to search through
     * @return the closest Station
     */
    private Station findClosestStation(LatLng location, Stations stations) {
        Station closestStation = null;
        double closestDistance = Double.MAX_VALUE;

        for (Station station : stations.stationBeanList) {
            double distance = SphericalUtil.computeDistanceBetween(location, station.getLocation());
            if (distance < closestDistance) {
                closestDistance = distance;
                closestStation = station;
            }
        }

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
