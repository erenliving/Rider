package com.erenlivingstone.rider.appscreens.location;

import android.app.Activity;

import com.erenlivingstone.rider.appscreens.BasePresenter;
import com.erenlivingstone.rider.appscreens.BaseView;
import com.erenlivingstone.rider.constants.LocationMode;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eren-DSK on 09/12/2016.
 */

public interface LocationContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void requestFineLocationPermission();

        void showLocationPermissionSnackbar();

        void onLocationFound(LatLng location);

    }

    interface Presenter extends BasePresenter {

        void onLocationButtonPressed(LocationMode locationMode, Activity activity);

        void fineLocationPermissionGranted(Activity activity);

    }

}
