package com.erenlivingstone.rider.appscreens.searchlocation;

import com.erenlivingstone.rider.appscreens.BasePresenter;
import com.erenlivingstone.rider.appscreens.BaseView;
import com.erenlivingstone.rider.constants.LocationMode;
import com.erenlivingstone.rider.constants.SearchMode;
import com.erenlivingstone.rider.data.model.Stations;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eren-DSK on 09/12/2016.
 */

public interface SearchLocationContract {

    interface View extends BaseView<Presenter> {

        void setSelectedSearchButton(SearchMode searchMode);

        void enableLocationButtons();

        void setLoadingIndicator(boolean active);

        void setLoadingIndicatorStatus(String status);

        boolean checkFineLocationPermission();

        boolean shouldShowPermissionRationale();

        void requestFineLocationPermission();

        void showLocationPermissionSnackbar();

        void startRequestLocationService();

        void onSearchReady(SearchMode searchMode, LatLng location, Stations stations);

    }

    interface Presenter extends BasePresenter {

        void onSearchModeButtonPressed(SearchMode searchMode);

        void onLocationButtonPressed(LocationMode locationMode);

        void fineLocationPermissionGranted();

    }

}
