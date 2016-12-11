package com.erenlivingstone.rider.appscreens.bikes;

import com.erenlivingstone.rider.appscreens.BasePresenter;
import com.erenlivingstone.rider.appscreens.BaseView;
import com.erenlivingstone.rider.data.model.Station;

/**
 * Created by Eren on 10/12/2016.
 */

public interface BikesContract {

    interface View extends BaseView<Presenter> {

        void showStationCard(Station station);

    }

    interface Presenter extends BasePresenter {

        void onAcceptCardSwipe();

        void onRejectCardSwipe();

    }

}
