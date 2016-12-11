package com.erenlivingstone.rider.appscreens.home;

import com.erenlivingstone.rider.appscreens.BasePresenter;
import com.erenlivingstone.rider.appscreens.BaseView;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;

/**
 * Created by Eren-DSK on 08/12/2016.
 */

public interface HomeContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void onSearchModeSelected(SearchMode searchMode);

        void onStationsLoaded(Stations stations);

    }

    interface Presenter extends BasePresenter {

        void onSearchButtonPressed(SearchMode searchMode);

    }

}
