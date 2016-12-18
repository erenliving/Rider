package com.erenlivingstone.rider.appscreens.bikes;

import com.daprlabs.aaron.swipedeck.SwipeDeck;
import com.erenlivingstone.rider.appscreens.card.SwipeDeckAdapter;
import com.erenlivingstone.rider.data.model.Station;
import com.erenlivingstone.rider.data.model.Stations;
import com.erenlivingstone.rider.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.Collections;

/**
 * Created by Eren on 10/12/2016.
 */

public class BikesPresenter implements BikesContract.Presenter,
        SwipeDeck.SwipeDeckCallback {

    private final BikesContract.View mBikesView;

    private SwipeDeckAdapter mSwipeDeckAdapter;

    private LatLng mLocation;
    private Stations mStations;
    private Station mCurrentStation;

    private boolean mFirstLoad = true;
    private int mStationIndex = 0;

    public BikesPresenter(BikesContract.View bikesView, SwipeDeckAdapter swipeDeckAdapter, LatLng
            location, Stations stations) {
        mBikesView = bikesView;
        mBikesView.setPresenter(this);
        mSwipeDeckAdapter = swipeDeckAdapter;
        mLocation = location;
        mStations = stations;
    }

    @Override
    public void start() {
        loadStation();
    }

    private void loadStation() {
        mBikesView.setLoadingIndicator(true);

        if (mFirstLoad) {
            calculateStationDistancesAndLastUpdated(mLocation, mStations);
            // Sort Stations by closest distance
            Collections.sort(mStations.stationBeanList);
            mFirstLoad = false;
        }
        // TODO: Do I need an additional case here if mCurrentStation is null? When would it become null?

        mBikesView.setLoadingIndicator(false);
    }

    @Override
    public SwipeDeckAdapter getSwipeDeckAdapter() {
        return mSwipeDeckAdapter;
    }

    private void calculateStationDistancesAndLastUpdated(LatLng location, Stations stations) {
        for (Station station : stations.stationBeanList) {
            station.setDistance(SphericalUtil.computeDistanceBetween(location, station.getLocation()));
            station.setLastUpdated(Utils.getFormattedTimestampForDisplay(station.getLastCommunicationTime()));
        }
    }

    private void incrementStationIndex() {
        mStationIndex++;
    }

    private LatLng getCurrentStationLocation() {
        if (mStationIndex >= mStations.stationBeanList.size()) {
            return null;
        } else {
            return mStations.stationBeanList.get(mStationIndex).getLocation();
        }
    }

    //region SwipeDeckCallback methods

    @Override
    public void cardSwipedLeft(long stableId) {
        incrementStationIndex();
        mBikesView.showRejectedAnimation();
    }

    @Override
    public void cardSwipedRight(long stableId) {
        mBikesView.disableCardSwiping();
        mBikesView.startNavigationToStation(mLocation, getCurrentStationLocation());
    }

    //endregion

}
