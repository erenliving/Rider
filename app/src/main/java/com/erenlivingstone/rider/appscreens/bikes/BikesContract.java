package com.erenlivingstone.rider.appscreens.bikes;

import com.erenlivingstone.rider.appscreens.BasePresenter;
import com.erenlivingstone.rider.appscreens.BaseView;
import com.erenlivingstone.rider.data.model.Station;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eren on 10/12/2016.
 */

public interface BikesContract {

    interface View extends BaseView<Presenter> {

        void startFetchAddress(BikesPresenter.AddressResultReceiver resultReceiver, LatLng location);

        void showStationCard(String stationName, String availableBikes, String distance,
                             String location, String lastCommunicationTime);

        void setStationAddressText(String address);

        void showAddressSuccessToast();

    }

    interface Presenter extends BasePresenter {

        void onAcceptCardSwipe();

        void onRejectCardSwipe();

    }

}
